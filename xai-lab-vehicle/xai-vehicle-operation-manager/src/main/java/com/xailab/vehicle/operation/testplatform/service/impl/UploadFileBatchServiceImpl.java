package com.xailab.vehicle.operation.testplatform.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.operation.api.feign.pojo.OssUploadRequest;
import com.xailab.vehicle.framework.common.exception.ServerException;
import com.xailab.vehicle.framework.common.utils.DateUtils;
import com.xailab.vehicle.operation.beeeval.entity.MaterialBatchInfoEntity;
import com.xailab.vehicle.operation.beeeval.entity.MaterialBatchLogInfoEntity;
import com.xailab.vehicle.operation.beeeval.service.MaterialBatchInfoService;
import com.xailab.vehicle.operation.beeeval.service.MaterialBatchLogInfoService;
import com.xailab.vehicle.operation.beeeval.util.CronScheduler;
import com.xailab.vehicle.operation.config.FileServerConfig;
import com.xailab.vehicle.operation.testplatform.entity.TestCaseMaterialEntity;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestCaseEntity;
import com.xailab.vehicle.operation.testplatform.entity.TestPlatformVehicleTestStateEntity;
import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeDataStateEnum;
import com.xailab.vehicle.operation.testplatform.enums.FunctionTreeTestCaseRateStateEnum;
import com.xailab.vehicle.operation.testplatform.service.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@DS("test_platform")
public class UploadFileBatchServiceImpl implements UploadFileBatchService {

    @Autowired
    private FileStorageService storageService;

    @Resource
    private MaterialBatchInfoService materialBatchInfoService;

    @Resource
    private MaterialBatchLogInfoService materialBatchLogInfoService;

    @Autowired
    private TestPlatformVehicleTestStateService testStateService;

    @Resource
    private TestCaseMaterialService testCaseMaterialService;

    @Resource
    private FileServerConfig fileServerConfig;

    @Resource
    private TestPlatformVehicleTestCaseService testPlatformVehicleTestCaseService;

    @Resource
    private TestPlatformFunctionTreeService testPlatformFunctionTreeService;

    private static final String DASH = "-";

    // 创建 OkHttpClient 实例
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS) // 连接超时时间
            .writeTimeout(3600, TimeUnit.SECONDS)  // 写入超时时间
            .readTimeout(3600, TimeUnit.SECONDS)   // 读取超时时间
            .build();

    /**
     * 异步批量导入
     * @param files
     * @param recordId
     * @param batchName
     * @param materialClassify
     * @return
     */
    @Override
    public String uploadFileBatch(MultipartFile[] files, Integer recordId,String batchName,Integer materialClassify,Date executionTime) {

        List<Path> savedPaths = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                Path savedPath = storageService.store(file);
                savedPaths.add(savedPath);
            }
        }

        /**
         *  创建异步批量上传任务
         */
        MaterialBatchInfoEntity materialBatchInfoEntity = new MaterialBatchInfoEntity();

        materialBatchInfoEntity.setBatchName(batchName);

        materialBatchInfoEntity.setMaterialClassify(materialClassify);

        materialBatchInfoEntity.setRecordId(recordId);

        materialBatchInfoEntity.setExecutionTime(executionTime);

        materialBatchInfoEntity.setCreateDate(new Date());

        materialBatchInfoEntity.setStatus(0);

        materialBatchInfoService.save(materialBatchInfoEntity);

        Date now = new Date();
        long diffInMillis = Math.abs(executionTime.getTime() - now.getTime());
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis);

        List<MaterialBatchLogInfoEntity> detailList = new ArrayList<>();

        for (MultipartFile file : files){
            if (!file.isEmpty()){
                MaterialBatchLogInfoEntity materialBatchLogInfo = new MaterialBatchLogInfoEntity();
                materialBatchLogInfo.setBatchNumber(materialBatchInfoEntity.getId());
                materialBatchLogInfo.setMaterialName(file.getOriginalFilename());
                materialBatchLogInfo.setMaterialType(isImageFile(file.getOriginalFilename())?1:2);
                materialBatchLogInfo.setStatus(2);
                materialBatchLogInfo.setCreateDate(new Date());
                detailList.add(materialBatchLogInfo);
            }
        }
        materialBatchLogInfoService.saveBatch(detailList);

        /**
         * 创建异步批量上传素材任务详情
         */
        CronScheduler.scheduleOnce(
            batchName,
            () -> {
                log.info("执行异步批量上传任务");
                String fileType;
                Long fileSize=0L;
                materialBatchInfoService.update(Wrappers.<MaterialBatchInfoEntity>lambdaUpdate()
                        .eq(MaterialBatchInfoEntity::getBatchName,batchName)
                        .set(MaterialBatchInfoEntity::getStatus,1));
                for (Path path : savedPaths) {

                    File folder = new File(path+"");
                    MultipartFile file = fileConvertMultipartFile(folder);
                    String[] split = file.getOriginalFilename().split("-");

                    Integer testCaseId = Integer.parseInt(split[0]);
                    TestPlatformVehicleTestStateEntity stateEntity = getTestStateEntity(recordId,testCaseId);
                    String originalFilename = file.getOriginalFilename();
                    log.info("上传的文件名称为：{}", originalFilename);
                    if (StringUtils.isBlank(originalFilename)) {
                        throw new ServerException("文件名称为空");
                    }
                    String fileName = generate();
                    if (isImageFile(originalFilename)) {
                        log.info("当前文件为图片资源");
                        fileType = "image";
                        fileSize = uploadBatchFileCore(file, fileName, fileType, "image/jpeg");
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            log.error("文件删除失败: " + path, e);
                        }
                    } else if (isVideoFile(originalFilename)) {
                        log.info("当前文件为视频资源");
                        fileType = "video";
                        fileSize = uploadBatchFileCore(file, fileName, fileType, "video/mp4");
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            log.error("文件删除失败: " + path, e);
                        }
                    } else {
                        throw new ServerException("文件类型错误");
                    }
                    log.info("文件大小：{}",fileSize);
                    //保存文件上传名称
                    TestCaseMaterialEntity entity = new TestCaseMaterialEntity();
                    entity.setTestStateId(stateEntity.getId());
                    entity.setFileType("image".equals(fileType)?0:1);
                    entity.setObjectName(fileName);
                    entity.setOriginalFilename(originalFilename);
                    entity.setFileSize(fileSize);
                    testCaseMaterialService.saveOrUpdateN(entity);
                    stateEntity.setTestCaseRate(FunctionTreeTestCaseRateStateEnum.Good.getValue());
                    testStateService.insertOrUpdate(stateEntity);
                    //更新功能id状态
                    TestPlatformVehicleTestCaseEntity testCaseEntity = testPlatformVehicleTestCaseService.selectById(testCaseId);
                    testPlatformFunctionTreeService.updateFunctionTreeStateAsync(recordId,testCaseEntity.getTaskDetail(), FunctionTreeDataStateEnum.SYNCHRONIZATION.getValue());
                }
                materialBatchInfoService.update(Wrappers.<MaterialBatchInfoEntity>lambdaUpdate()
                        .eq(MaterialBatchInfoEntity::getBatchName,batchName)
                        .set(MaterialBatchInfoEntity::getStatus,2));
                log.info("同步任务执行完成：{},完成时间：{}",batchName, new Date());
            }, seconds, TimeUnit.SECONDS
        );
        return "已创建同步任务";
    }

    private Long uploadBatchFileCore(MultipartFile file, String fileName,String fileType,String media) {

        materialBatchLogInfoService.update(Wrappers.<MaterialBatchLogInfoEntity>lambdaUpdate()
                .eq(MaterialBatchLogInfoEntity::getMaterialName, file.getOriginalFilename())
                .eq(MaterialBatchLogInfoEntity::getMaterialType, isImageFile(file.getOriginalFilename())?1:2)
                .set(MaterialBatchLogInfoEntity::getStatus,0));

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
            materialBatchLogInfoService.update(Wrappers.<MaterialBatchLogInfoEntity>lambdaUpdate()
                    .eq(MaterialBatchLogInfoEntity::getMaterialName, file.getOriginalFilename())
                    .eq(MaterialBatchLogInfoEntity::getMaterialType, isImageFile(file.getOriginalFilename())?1:2)
                    .set(MaterialBatchLogInfoEntity::getStatus,1));
        } catch (IOException e) {
            materialBatchLogInfoService.update(Wrappers.<MaterialBatchLogInfoEntity>lambdaUpdate()
                    .eq(MaterialBatchLogInfoEntity::getMaterialName, file.getOriginalFilename())
                    .eq(MaterialBatchLogInfoEntity::getMaterialType, isImageFile(file.getOriginalFilename())?1:2)
                    .set(MaterialBatchLogInfoEntity::getStatus,-1));
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
     * 将文件转换为MultipartFile对象
     * @param file
     * @return
     */
    public static MultipartFile fileConvertMultipartFile(File file){
        // 创建一个DiskFileItemFactory对象
        // DiskFileItemFactory factory = new DiskFileItemFactory();

        // 创建一个DiskFileItem对象，注意：这里的文件名会作为上传文件的名字
        DiskFileItem fileItem = new DiskFileItem("file", "application/octet-stream", false, file.getName(), (int) file.length(), file.getParentFile());

        // 将文件内容写入DiskFileItem
        try {
            fileItem.getOutputStream().write(java.nio.file.Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 返回MultipartFile
        return new MultipartFile() {
            @Override
            public String getName() {
                return fileItem.getFieldName();
            }

            @Override
            public String getOriginalFilename() {
                return fileItem.getName();
            }

            @Override
            public String getContentType() {
                return fileItem.getContentType();
            }

            @Override
            public boolean isEmpty() {
                return fileItem.getSize() == 0;
            }

            @Override
            public long getSize() {
                return fileItem.getSize();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return java.nio.file.Files.readAllBytes(file.toPath());
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return fileItem.getInputStream();
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                try {
                    fileItem.write(dest);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
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
