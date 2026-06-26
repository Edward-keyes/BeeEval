package com.xailab.vehicle.operation.testplatform.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.operation.api.feign.pojo.OSSFileDeleteRequest;
import com.xailab.operation.api.feign.pojo.OSSResponse;
import com.xailab.operation.api.feign.pojo.OSSResult;
import com.xailab.operation.api.feign.pojo.OssUploadRequest;
import com.xailab.vehicle.framework.common.exception.ServerException;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import com.xailab.vehicle.framework.common.utils.JsonUtils;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.framework.security.user.SecurityUser;
import com.xailab.vehicle.framework.security.user.UserDetail;
import com.xailab.vehicle.operation.beeeval.service.MaterialBatchLogInfoService;
import com.xailab.vehicle.operation.config.FileServerConfig;
import com.xailab.vehicle.operation.system.utils.Okhttp3Utils;
import com.xailab.vehicle.operation.testplatform.convert.TestCaseMaterialConvert;
import com.xailab.vehicle.operation.testplatform.convert.TestPlatformVehicleTestStateConvert;
import com.xailab.vehicle.operation.testplatform.dao.FunctionTreeStateDao;
import com.xailab.vehicle.operation.testplatform.dao.TestCaseMaterialDao;
import com.xailab.vehicle.operation.testplatform.dao.TestPlatformVehicleTestCaseDao;
import com.xailab.vehicle.operation.testplatform.dao.TestPlatformVehicleTestStateDao;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeStateEntity;
import com.xailab.vehicle.operation.testplatform.entity.TestCaseMaterialEntity;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestCaseEntity;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestStateEntity;
import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeDataStateEnum;
import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeTestCaseRateStateEnum;
import com.xailab.vehicle.operation.testplatform.pojo.request.TestCaseMaterialShowRequest;
import com.xailab.vehicle.operation.testplatform.pojo.response.TestStateInfoResponse;
import com.xailab.vehicle.operation.testplatform.service.*;
import com.xailab.vehicle.operation.testplatform.vo.TestCaseMaterialVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: TestCaseMaterialServiceImpl
 * @Description:
 * @author: liulin
 * @date: 2025/5/29 0:04
 */

@Service
@Slf4j
@DS("test_platform")
public class TestCaseMaterialServiceImpl extends BaseServiceImpl<TestCaseMaterialDao, TestCaseMaterialEntity> implements TestCaseMaterialService {

    @Resource
    private FileServerConfig fileServerConfig;

    @Autowired
    private TestPlatformVehicleTestStateDao testStateDao;

    @Autowired
    private TestPlatformVehicleTestStateService testStateService;

    @Resource
    private TestPlatformVehicleTestCaseDao testPlatformVehicleTestCaseDao;

    @Resource
    private TestPlatformFunctionTreeService testPlatformFunctionTreeService;

    @Resource
    private FunctionTreeStateDao functionTreeStateDao;



    // 创建 OkHttpClient 实例
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS) // 连接超时时间
            .writeTimeout(3600, TimeUnit.SECONDS)  // 写入超时时间
            .readTimeout(3600, TimeUnit.SECONDS)   // 读取超时时间
            .build();

    private static final String DASH = "-";
    private static final String DOT = ".";

    /**
     * 文件上传
     * @param file
     * @param recordId
     * @param testCaseId
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file, Integer recordId, Integer testCaseId) {
        TestPlatformVehicleTestStateEntity stateEntity = getTestStateEntity(recordId, testCaseId);
        String originalFilename = file.getOriginalFilename();
        log.info("上传的文件名称为：{}",originalFilename);
        if (StringUtils.isBlank(originalFilename)){
            throw new ServerException("文件名称为空");
        }
        //生成文件名称
//        String fileSuffix = getFileSuffix(originalFilename);
        String fileName = generate();
        String fileType = "";
        Long fileSize = 0L;
        log.info("生成的文件名称为：{}",fileName);
        if (isImageFile(originalFilename)){
            log.info("当前文件为图片资源");
            fileType= "image";
            fileSize = uploadFileCore(file, fileName,fileType,"image/jpeg");
        }else if (isVideoFile(originalFilename)){
            log.info("当前文件为视频资源");
            fileType= "video";
            fileSize =  uploadFileCore(file,fileName,"video","video/mp4");
        }else {
            throw new ServerException("文件类型错误");
        }
        //保存文件上传名称
        TestCaseMaterialEntity entity = new TestCaseMaterialEntity();
        entity.setTestStateId(stateEntity.getId());
        entity.setFileType("image".equals(fileType)?0:1);
        entity.setObjectName(fileName);
        entity.setOriginalFilename(originalFilename);
        entity.setFileSize(fileSize);
        saveOrUpdate(entity);
        //如果状态为缺数据或者没有素材
        if (StringUtils.isBlank(stateEntity.getTestCaseRate()) ||
                FunctionTreeTestCaseRateStateEnum.NA.equals(stateEntity.getTestCaseRate())  ||
                FunctionTreeTestCaseRateStateEnum.Poor.equals(stateEntity.getTestCaseRate())){
            stateEntity.setTestCaseRate(FunctionTreeTestCaseRateStateEnum.Good.getValue());
            testStateDao.insertOrUpdate(stateEntity);
            //更新功能id状态
            TestPlatformVehicleTestCaseEntity testCaseEntity = testPlatformVehicleTestCaseDao.selectById(testCaseId);
            testPlatformFunctionTreeService.updateFunctionTreeStateAsync(recordId,testCaseEntity.getTaskDetail(), FunctionTreeDataStateEnum.SYNCHRONIZATION.getValue());
        }
        return fileName;
    }


    /**
     * 查询素材详情
     * @param recordId
     * @param testCaseId
     * @return
     */
    @Override
    public TestStateInfoResponse findMaterialList(Integer recordId, Integer testCaseId) {
        TestPlatformVehicleTestStateEntity testStateEntity = getTestStateEntity(recordId, testCaseId);
        List<TestCaseMaterialEntity> testCaseMaterialEntities = baseMapper.selectList(Wrappers.<TestCaseMaterialEntity>lambdaQuery()
                .eq(TestCaseMaterialEntity::getTestStateId, testStateEntity.getId())
                .orderByDesc(TestCaseMaterialEntity::getCreateTime)
        );
        List<TestCaseMaterialEntity> showEntities = testCaseMaterialEntities.stream().filter(it -> Objects.nonNull(it.getIsShow()) && it.getIsShow()).toList();
        if ((!CollectionUtils.isEmpty(testCaseMaterialEntities)) && CollectionUtils.isEmpty(showEntities)){
            TestCaseMaterialEntity materialEntity = testCaseMaterialEntities.getLast();
            materialEntity.setIsShow(true);
        }

        List<TestCaseMaterialVo> materialVos = TestCaseMaterialConvert.INSTANCE.convertList(testCaseMaterialEntities);
        TestStateInfoResponse testStateInfoResponse = TestPlatformVehicleTestStateConvert.INSTANCE.convertRes(testStateEntity, materialVos);
        return testStateInfoResponse;
    }


    /**
     * 修改状态
     * @param recordId
     * @param testCaseId
     */
    @Override
    public void editState(Integer recordId, Integer testCaseId,String state) {
        FunctionTreeTestCaseRateStateEnum stateEnum = FunctionTreeTestCaseRateStateEnum.paseEnum(state);
        if (Objects.isNull(stateEnum)){
            throw new ServerException("状态错误");
        }
        TestPlatformVehicleTestStateEntity testStateEntity = getTestStateEntity(recordId, testCaseId);
//        if (!FunctionTreeCaseFileMaterialStateEnum.NA.equals(testStateEntity.getTestCaseRate()) &&
//                stateEnum.equals(FunctionTreeCaseFileMaterialStateEnum.NA)){
//            throw new ServerException("状态错误");
//        }
        if (StringUtils.isNotBlank(testStateEntity.getTestCaseRate()) && stateEnum.equals(testStateEntity.getTestCaseRate())){
            log.info("状态未改变");
            return;
        }
        testStateEntity.setTestCaseRate(stateEnum.getValue());
        testStateDao.insertOrUpdate(testStateEntity);
        //更新功能id状态
        TestPlatformVehicleTestCaseEntity testCaseEntity = testPlatformVehicleTestCaseDao.selectById(testCaseId);
        testPlatformFunctionTreeService.updateFunctionTreeStateAsync(recordId,testCaseEntity.getTaskDetail(), FunctionTreeDataStateEnum.SYNCHRONIZATION.getValue());
    }


    @Override
    public String queryPhoto(String photoName) {

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        try {
            String data = Okhttp3Utils.getData(fileServerConfig.getGetImageUrl() + "?file_id=" + photoName, headers);
            OSSResponse ossResponse = JsonUtils.parseObject(data, OSSResponse.class);
            log.info("获取图片返回参数：photoName:{},data:{}",photoName,data);
            if (Objects.isNull(ossResponse) || StringUtils.isBlank(ossResponse.getUrl())){
                throw new ServerException("获取文件地址失败");
            }
            return ossResponse.getUrl();
        } catch (IOException e) {
            log.info("获取图片文件地址异常：{}",e.getMessage(),e);
            throw new ServerException("获取文件地址异常");
        }

    }

    @Override
    public String queryVideo(String videoName) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        try {
            String data = Okhttp3Utils.getData(fileServerConfig.getGetPlayList() + "?file_id=" + videoName, headers);
            OSSResponse ossResponse = JsonUtils.parseObject(data, OSSResponse.class);
            log.info("获取视频返回参数：photoName:{},data:{}",videoName,data);
            if (Objects.isNull(ossResponse) || StringUtils.isBlank(ossResponse.getPlaylist_url())){
                throw new ServerException("获取文件地址失败");
            }
            return ossResponse.getPlaylist_url();
        } catch (IOException e) {
            log.info("获取视频文件地址异常：{}",e.getMessage(),e);
            throw new ServerException("获取文件地址异常");
        }
    }

    /**
     * 设置素材显示
     * @param request
     */
    @Override
    public void setMaterialShow(TestCaseMaterialShowRequest request) {
        //查询已经显示的素材
        TestPlatformVehicleTestStateEntity stateEntity = getTestStateEntity(request.getRecordId(), request.getTestCaseId());
        List<TestCaseMaterialEntity> testCaseMaterialEntities = baseMapper.selectList(Wrappers.<TestCaseMaterialEntity>lambdaQuery()
                .eq(TestCaseMaterialEntity::getTestStateId, stateEntity.getId())
                .eq(TestCaseMaterialEntity::getIsShow, true)
        );
        List<Long> newShowMaterialIds = request.getMaterialIds();
        List<Long> noShowMaterialIds = testCaseMaterialEntities.stream().map(TestCaseMaterialEntity::getId).filter(it -> !newShowMaterialIds.contains(it)).toList();
        if (!CollectionUtils.isEmpty(noShowMaterialIds)){
            //更新素材显示状态为不显示
            baseMapper.update(Wrappers.<TestCaseMaterialEntity>lambdaUpdate()
                    .in(TestCaseMaterialEntity::getId, noShowMaterialIds)
                    .set(TestCaseMaterialEntity::getIsShow, false));
        }
        if (!CollectionUtils.isEmpty(newShowMaterialIds)){
            //更新素材显示状态为显示
            baseMapper.update(Wrappers.<TestCaseMaterialEntity>lambdaUpdate()
                    .in(TestCaseMaterialEntity::getId, newShowMaterialIds)
                    .set(TestCaseMaterialEntity::getIsShow, true));
        }
    }

    /**
     * 素材删除
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteMaterial(List<Long> ids) {
        try {
            UserDetail user = SecurityUser.getUser();
            //查询列表
            List<TestCaseMaterialEntity> list = baseMapper.selectBatchIds(ids);
            if (CollectionUtils.isEmpty(list)){
                throw new ServerException("文件不存在");
            }
            //批量删除文件
            List<String> files = list.stream().map(TestCaseMaterialEntity::getObjectName).filter(StringUtils::isNotBlank).toList();
            OSSFileDeleteRequest ossFileDeleteRequest = new OSSFileDeleteRequest();
            ossFileDeleteRequest.setFile_ids(files);
            String result = Okhttp3Utils.deleteData(fileServerConfig.getDeleteFiles(), JsonUtils.toJsonString(ossFileDeleteRequest), null);
            log.info("批量删除文件结果：{}",result);
            OSSResult ossResult = JsonUtils.parseObject(result, OSSResult.class);
            if (Objects.isNull(ossResult) || Boolean.FALSE.equals(ossResult.getSuccess())){
                throw new ServerException("批量删除文件失败");
            }
            //删除表
            int number = baseMapper.deleteByIds(ids);
            log.info("删除文件列，数量为：{}，文件为：{}，用户为：{}",number,files,user.getUsername());
        }catch (Exception e){
            log.error("删除文件异常：{}",e.getMessage(),e);
            throw new ServerException("文件删除异常");
        }
    }


    @DS("test_platform")
    @Override
    public void saveOrUpdateN(TestCaseMaterialEntity entity){

        baseMapper.insertOrUpdate(entity);

    }

    /**
     * 获取测试状态详情
     * @param recordId
     * @param testCaseId
     * @return
     */
    private TestPlatformVehicleTestStateEntity getTestStateEntity(Integer recordId, Integer testCaseId) {
        //获取当前的stateId
        TestPlatformVehicleTestStateEntity stateEntity = testStateService.getOne(Wrappers.<TestPlatformVehicleTestStateEntity>lambdaQuery()
                .eq(TestPlatformVehicleTestStateEntity::getRecordId, recordId)
                .eq(TestPlatformVehicleTestStateEntity::getTestcaseId, testCaseId)
        );
        if (Objects.isNull(stateEntity)){
            //创建state
            stateEntity = new TestPlatformVehicleTestStateEntity();
            stateEntity.setTestStatus(0);
            stateEntity.setRecordId(recordId);
            stateEntity.setTestcaseId(testCaseId);
            stateEntity.setIsSuccessful(-1);
            testStateService.save(stateEntity);
        }
        return stateEntity;
    }


    /**
     * 上传文件详情
     * @param file
     * @param fileName
     * @throws IOException
     */
    private Long uploadFileCore(MultipartFile file, String fileName,String fileType,String media) {
        Long fileSize = 0L;
        try {
            byte[] bytes = file.getBytes();
            fileSize = (long) bytes.length;
            OssUploadRequest ossUploadRequest = new OssUploadRequest();
            ossUploadRequest.setFile_type(fileType);
            ossUploadRequest.setFile_id(fileName);
            log.info(ossUploadRequest.getFile_id());
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file_id", ossUploadRequest.getFile_id())
                    .addFormDataPart("file_type", ossUploadRequest.getFile_type())
                    .addFormDataPart("uploaded_file", file.getName(),
                            RequestBody.create(bytes, MediaType.parse(media)))
                    .build();

            // 构建请求
            Request request = new Request.Builder()
                    .url(fileServerConfig.getSaveFileToOSS())
                    .post(requestBody)
                    .build();

            // 发送请求
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            // 打印响应
            if (response.body() != null) {
                String string = response.body().toString();
                log.info("file upload result:{}",string);
            }
        } catch (IOException e) {
            log.error("上传文件失败：{}",e.getMessage(),e);
            throw new ServerException("上传文件失败");
        }
        return fileSize;
    }



    // 判断文件是否为图片
    private boolean isImageFile(String fileName) {
        if (StringUtils.isBlank(fileName)){
            return false;
        }
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                fileName.endsWith(".bmp");
    }

    // 判断文件是否为视频
    private boolean isVideoFile(String fileName) {
        if (StringUtils.isBlank(fileName)){
            return false;
        }
        return fileName.endsWith(".mp4") || fileName.endsWith(".avi") ||
                fileName.endsWith(".mov") || fileName.endsWith(".mkv") ||
                fileName.endsWith(".flv");
    }

    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public String getFileSuffix(String fileName){
       return fileName.substring(fileName.lastIndexOf(".")+1);
    }

    /**
     * 生成文件名称
     * @param filenameExtension
     * @return
     */
    public String generate(String filenameExtension) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DateUtils.format(new Date()))
                .append(System.currentTimeMillis()).append(DASH).append(UUID.fastUUID()).append(DOT).append(filenameExtension);
        log.info("生成文件名: {}", stringBuilder);
        return stringBuilder.toString();
    }
    /**
     * 生成文件名称
     * @return
     */
    public String generate() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DateUtils.format(new Date()))
                .append(System.currentTimeMillis()).append(DASH).append(UUID.fastUUID());
        log.info("生成文件名: {}", stringBuilder);
        return stringBuilder.toString();
    }


}
