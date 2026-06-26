package com.xailab.vehicle.xaivehicledata.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xailab.operation.api.feign.pojo.OSSFileDeleteRequest;
import com.xailab.operation.api.feign.pojo.OSSResult;
import com.xailab.vehicle.feign.common.PageResultBee;
import com.xailab.vehicle.feign.pojo.treem.*;
import com.xailab.vehicle.xaicommon.utils.DateUtils;
import com.xailab.vehicle.xaicommon.utils.JsonUtils;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.common.QueryCommonPage;
import com.xailab.vehicle.xaivehicledata.config.ALiYunOssConfig;
import com.xailab.vehicle.xaivehicledata.dao.*;
import com.xailab.vehicle.xaivehicledata.entity.*;
import com.xailab.vehicle.xaivehicledata.entity.request.OssUploadRequest;
import com.xailab.vehicle.xaivehicledata.service.FunctionTreeManagerService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class FunctionTreeManagerServiceImpl implements FunctionTreeManagerService {


    @Resource
    private FunctionTreeDao functionTreeDao;

    @Resource
    private FunctionThreeTagDao functionThreeTagDao;

    @Resource
    private FunctionTreeCaseDao functionTreeCaseDao;

    @Resource
    private FunctionCaseVehicleDao functionCaseVehicleDao;

    @Resource
    private FunctionCaseMaterialDao functionCaseMaterialDao;

    @Resource
    private BaseInfoDao baseInfoDao;

    @Resource
    private ALiYunOssConfig aLiYunOssConfig;

    // 创建 OkHttpClient 实例
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS) // 连接超时时间
            .writeTimeout(3600, TimeUnit.SECONDS)  // 写入超时时间
            .readTimeout(3600, TimeUnit.SECONDS)   // 读取超时时间
            .build();
    private static final String DASH = "-";
    private static final String DOT = ".";


    /**
     * 分页查询功能树列表
     * @param pageQuery
     * @return
     */
    @Override
    public PageResultBee<FunctionTreeQueryListResponse> queryFunctionTreeList(FunctionTreeListPageRequest pageQuery) {
        //获取page
        IPage<FunctionThreeTagEntity> page = new QueryCommonPage<FunctionThreeTagEntity>().getPage(pageQuery);
        //查询列表
        IPage<FunctionTreeQueryListResponse> list = functionThreeTagDao.queryFunctionTreePage(page, pageQuery.getTagNumber(), pageQuery.getTagName(), pageQuery.getVehicleId());
        List<FunctionTreeQueryListResponse> records = list.getRecords();
        for (FunctionTreeQueryListResponse record : records) {
            //查询对应的用例数据
            List<FunctionTreeCaseResponse> treeCaseResponses = functionTreeCaseDao.queryFunctionTreeCaseList(record.getTagNumber(), pageQuery.getVehicleId());
            record.setFunctionTreeCase(treeCaseResponses);
        }
        return new PageResultBee<>(records, list.getTotal());
    }


    /**
     * 功能树更新
     */
    @Override
    public Result<Void> functionTreeUpdate(FunctionTreeUpdateRequest request) {
        FunctionThreeTagEntity functionThreeTagEntity = functionThreeTagDao.queryOneByTagNumber(request.getTagNumber());
        if (Objects.isNull(functionThreeTagEntity)) {
            log.info("功能树信息不存在：{}", request.getTagNumber());
            return Result.error("功能树信息不存在");
        }
        //查询当前功能树信息
        FunctionTreeEntity treeEntity = functionTreeDao.queryOneByThreeTagIdAndVehicleId(functionThreeTagEntity.getId(), request.getVehicleId());
        if (Objects.isNull(treeEntity)){
            log.info("对应功能树信息为空:{}", request.getTagNumber());
            return Result.error("对应功能树信息为空");
        }
        //编辑
        functionThreeTagEntity.setTagName(request.getTagName());
        functionThreeTagEntity.setTagNameEn(request.getTagNameEn());
        functionThreeTagEntity.setDescription(request.getDescription());
        functionThreeTagEntity.setDescriptionEn(request.getDescriptionEn());
        functionThreeTagDao.updateById(functionThreeTagEntity);
        //编辑功能树信息
        treeEntity.setFunctionList(request.getFunctionList());
        functionTreeDao.updateById(treeEntity);
        return Result.ok();
    }


    /**
     * 编辑用例
     */
    @Override
    public Result<Void> editFunctionTreeCase(FunctionTreeCaseEditRequest request) {
        //查询车辆id是否存在
        BaseInfoEntity baseInfo = baseInfoDao.selectById(request.getVehicleId());
        if (Objects.isNull(baseInfo)){
            log.info("车辆信息不存在：{}", request.getVehicleId());
            return Result.error("车辆信息不存在");
        }
        //查询功能树信息
        FunctionTreeCaseEntity functionTreeCaseEntity = functionTreeCaseDao.selectOne(Wrappers.<FunctionTreeCaseEntity>lambdaQuery()
                .eq(FunctionTreeCaseEntity::getId, request.getId())
        );
        if (Objects.isNull(functionTreeCaseEntity)) {
            log.info("功能树用例不存在：{}", request.getId());
            return Result.error("功能树用例不存在");
        }
        FunctionCaseVehicleEntity vehicleEntity = functionCaseVehicleDao.selectOne(Wrappers.<FunctionCaseVehicleEntity>lambdaQuery()
                .eq(FunctionCaseVehicleEntity::getCaseId, functionTreeCaseEntity.getId())
                .eq(FunctionCaseVehicleEntity::getVehicleId, request.getVehicleId())
        );
        if (Objects.isNull(vehicleEntity)) {
            log.info("功能树用例对应车状态为空：{},创建", request.getId());
           //创建
            vehicleEntity = new FunctionCaseVehicleEntity();
            vehicleEntity.setCaseId(functionTreeCaseEntity.getId());
            vehicleEntity.setVehicleId(request.getVehicleId());
            vehicleEntity.setIsShow(request.getIsShow());
            vehicleEntity.setFunctionEvaluation(request.getFunctionEvaluation());
            vehicleEntity.setCaseOptions(request.getCaseOptions());
            functionCaseVehicleDao.insert(vehicleEntity);
        }else {
            //修改
            vehicleEntity.setIsShow(request.getIsShow());
            vehicleEntity.setFunctionEvaluation(request.getFunctionEvaluation());
            vehicleEntity.setCaseOptions(request.getCaseOptions());
            functionCaseVehicleDao.updateById(vehicleEntity);
        }
        //修改
        functionTreeCaseEntity.setCaseContent(request.getCaseContent());
        functionTreeCaseEntity.setCaseContentEn(request.getCaseContentEn());
        functionTreeCaseDao.updateById(functionTreeCaseEntity);
        return Result.ok();
    }


    /**
     * 用例素材查询
     */
    @Override
    public List<FunctionTreeCaseMaterialResponse> queryFunctionTreeCaseMaterial(FunctionTreeCaseMaterialQueryRequest request) {
        List<FunctionTreeCaseMaterialResponse> caseMaterialResponses = functionCaseMaterialDao.queryFunctionTreeCaseMaterial(request.getFunctionTreeCaseId(), request.getVehicleId());
        log.info("查询到的用例素材为：{}", JsonUtils.objToJson(caseMaterialResponses));
        return caseMaterialResponses;
    }



    /**
     * 用例素材上传
     * @param file
     * @param vehicleId
     * @param functionTreeCaseId
     */
    @Override
    public Result<Void> functionTreeCaseMaterialUpload(MultipartFile file, Long vehicleId, Long functionTreeCaseId) {
        BaseInfoEntity baseInfo = baseInfoDao.selectById(vehicleId);
        if (Objects.isNull(baseInfo)){
            log.info("车辆信息不存在：{}", vehicleId);
            return Result.error("车辆信息不存在");
        }
        FunctionTreeCaseEntity functionTreeCaseEntity = functionTreeCaseDao.selectOne(Wrappers.<FunctionTreeCaseEntity>lambdaQuery()
                .eq(FunctionTreeCaseEntity::getId, functionTreeCaseId)
        );
        if (Objects.isNull(functionTreeCaseEntity)) {
            log.info("功能树用例不存在：{}", functionTreeCaseId);
            return Result.error("功能树用例不存在");
        }
        //查询是否存在素材，如果存在不可进行上传
        List<String> strings = functionCaseMaterialDao.selectUrlByCaseId(functionTreeCaseId, vehicleId);
        if (!CollectionUtils.isEmpty(strings)){
            log.info("功能树用例素材已经存在：{}", functionTreeCaseId);
            return Result.error("功能树用例素材已经存在");
        }
        //上传文件
        String originalFilename = file.getOriginalFilename();
        log.info("上传的文件名称为：{}",originalFilename);
        if (StringUtils.isBlank(originalFilename)){
            return Result.error("文件名称为空");
        }
        //生成文件名称
        String fileName = generate();
        String fileType = "";
        Result<Long> fileSize = Result.ok();
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
           return Result.error("文件类型错误");
        }

        //文件上传失败
        if (!fileSize.isOk()){
            log.info("文件上传失败");
            return Result.error("文件上传失败");
        }
        //保存文件上传名称
        FunctionCaseMaterialEntity entity = new FunctionCaseMaterialEntity();
        entity.setVehicleId(vehicleId);
        entity.setFunctionTreeCaseId(functionTreeCaseId);
        entity.setMaterialType("image".equals(fileType)?"0":"1");
        entity.setMaterialUrl(fileName);
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        functionCaseMaterialDao.insert(entity);
        return Result.ok();
    }

    /**
     * 删除功能树用例素材
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Void> deleteFunctionTreeCaseMaterial(Long id) {
        try {
            //查询列表
            FunctionCaseMaterialEntity materialEntity = functionCaseMaterialDao.selectById(id);
            if (Objects.isNull(materialEntity)){
                log.info("功能树用例素材不存在：{}", id);
                return Result.error("功能树用例素材不存在");
            }
            //批量删除文件
            OSSFileDeleteRequest ossFileDeleteRequest = new OSSFileDeleteRequest();
            ossFileDeleteRequest.setFile_ids(List.of(materialEntity.getMaterialUrl()));
            String result = deleteData(aLiYunOssConfig.getDeleteFiles(), JsonUtils.objToJson(ossFileDeleteRequest), null);
            log.info("批量删除文件结果：{}",result);
            OSSResult ossResult = JsonUtils.jsonToObj(result, OSSResult.class);
            if (Objects.isNull(ossResult) || Boolean.FALSE.equals(ossResult.getSuccess())){
                return Result.error("删除文件失败");
            }
            //删除表
            functionCaseMaterialDao.deleteById(id);
            log.info("删除文件，文件为：{}",materialEntity.getMaterialUrl());
            return Result.ok();
        }catch (Exception e){
            log.error("删除文件异常：{}",e.getMessage(),e);
            return Result.error("文件删除异常");
        }
    }


    /**
     * 上传文件详情
     * @param file
     * @param fileName
     * @throws IOException
     */
    private Result<Long> uploadFileCore(MultipartFile file, String fileName,String fileType,String media) {
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
                    .url(aLiYunOssConfig.getSaveFileToOSS())
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
            return Result.error("上传文件失败");
        }
        return Result.ok(fileSize);
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
     * 执行POST请求
     *
     * @param url       请求URL
     * @param json      请求体数据(JSON格式)
     * @param headerMap 请求头参数
     * @return 响应数据
     * @throws IOException IO异常
     */
    public String deleteData(String url, String json, Map<String, String> headerMap) throws IOException {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request.Builder builder = new Request.Builder().url(url);
        addHeaders(builder, headerMap);
        Request request = builder.delete(requestBody).build();
        try (ResponseBody body = client.newCall(request).execute().body()) {
            return body.string();
        }
    }

    /**
     * 添加请求头参数
     *
     * @param builder   请求构建器
     * @param headerMap 请求头参数
     */
    private void addHeaders(Request.Builder builder, Map<String, String> headerMap) {
        if (MapUtils.isNotEmpty(headerMap)) {
            headerMap.forEach(builder::addHeader);
        }
    }
}
