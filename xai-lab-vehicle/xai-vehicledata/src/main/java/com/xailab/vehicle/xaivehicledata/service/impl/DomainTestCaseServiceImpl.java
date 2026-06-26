package com.xailab.vehicle.xaivehicledata.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xailab.vehicle.xaicommon.utils.ExcelUtils;
import com.xailab.vehicle.xaicommon.utils.SnowflakeIdGenerator;
import com.xailab.vehicle.xaivehicledata.config.ALiYunOssConfig;
import com.xailab.vehicle.xaivehicledata.dao.DomainTestCaseDao;
import com.xailab.vehicle.xaivehicledata.entity.DomainTestCaseEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.CaseRequest;
import com.xailab.vehicle.xaivehicledata.entity.request.OssUploadRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.DomainTestCaseResponse;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionTreeExcelData;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionalVideoVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.TestCaseExcelData;
import com.xailab.vehicle.xaivehicledata.service.ALiYunOSSService;
import com.xailab.vehicle.xaivehicledata.service.BaseInfoService;
import com.xailab.vehicle.xaivehicledata.service.DomainIndexService;
import com.xailab.vehicle.xaivehicledata.service.DomainTestCaseService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.xailab.vehicle.xaivehicledata.service.impl.FunctionOneTagServiceImpl.fileConvertMultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class DomainTestCaseServiceImpl extends ServiceImpl<DomainTestCaseDao, DomainTestCaseEntity> implements DomainTestCaseService {

    @Autowired
    DomainTestCaseDao domainTestCaseDao;

    @Autowired
    BaseInfoService baseInfoService;

    @Autowired
    DomainIndexService domainIndexService;

    @Resource
    private ALiYunOSSService aliYunOSSService;

    private final ALiYunOssConfig ossConfig;

    // 创建 OkHttpClient 实例
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(50000, TimeUnit.SECONDS) // 连接超时时间
            .writeTimeout(50000, TimeUnit.SECONDS)  // 写入超时时间
            .readTimeout(50000, TimeUnit.SECONDS)   // 读取超时时间
            .build();

    private static final SnowflakeIdGenerator snowflakeIdGenerator
            = new SnowflakeIdGenerator(0, 0);


    @Override
    public DomainTestCaseEntity getByVehicleIdAndDomainIndexId(String vehicleId, String domainIndexId) {

        DomainTestCaseEntity domainTestCaseEntity = domainTestCaseDao.getByVehicleIdAndDomainIndexId(vehicleId, domainIndexId);

        return domainTestCaseEntity;
    }

    @Override
    public void domainTestCaseDataInput(String path) {

        File folder = new File(path);

        Map<String,String> brandVehicleVersion = baseInfoService.getBrandVehicleVersionMap();

        Map<String,String> domainIndex = domainIndexService.getDomainIndexMap();

        // 检查路径是否为文件夹
        if (folder.isDirectory()) {
            // 获取文件夹中的所有文件和子文件夹
            // 使用自定义过滤器过滤文件
            File[] files = folder.listFiles((dir,name) ->
                    !name.startsWith("~$") &&
                            !name.startsWith("._"));

            if (files != null) {
                for (File file : files) {

                    if (file.isFile()) {
                        log.info("文件: " + file.getName());
                        if (file.getName().endsWith(".xlsx") || file.getName().endsWith(".xls")){
                            // 将文件转换为MultipartFile对象
                            MultipartFile cMultiFile = fileConvertMultipartFile(file);
                            log.info("转换完成");


                            // 读取Excel文件并保存到数据库
//                            ReadExcel(cMultiFile,brandVehicleVersion,domainIndex);
                        }
                    } else if (file.isDirectory()) {
                        String vehicleId = brandVehicleVersion.get(file.getName());
                        log.info("文件夹: " + file.getName());
                        processFolder(file,vehicleId,domainIndex);
                    }
                }
            } else {
                log.info("文件夹为空");
            }
        } else {
            log.info("指定路径不是一个文件夹");
        }

    }

    @Override
    public List<DomainTestCaseResponse> queryCaseByVehicleIdAndDomainIndexId(CaseRequest caseRequest) {

        List<DomainTestCaseResponse> domainTestCaseResponses = domainTestCaseDao.queryCaseByVehicleIdAndDomainIndexId(caseRequest.getDomainIndexId(),caseRequest.getVehicleId(),caseRequest.getLanguage());

//        int numberOfThreads = Runtime.getRuntime().availableProcessors();
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

//        for (DomainTestCaseResponse domainTestCaseResponse : domainTestCaseResponses) {
//            executorService.submit(() -> {
//                domainTestCaseResponse.setScore(domainTestCaseResponse.getScore());
//                domainTestCaseResponse.setUrl(aliYunOSSService.queryPhoto(domainTestCaseResponse.getUrl()));
//            });
//        }
//        executorService.shutdown(); // 关闭线程池
//        try {
//            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // 等待所有任务完成
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        return domainTestCaseResponses;
    }

    /**
     * 读取Excel不同sheet中的数据文件并保存到数据库
     * @param file
     */
    public void ReadExcel(MultipartFile file,Map<String,String> brandVehicleVersion,Map<String,String> domainIndex){

        Map<String, JSONArray> map = null;
        try {
            map = ExcelUtils.readFileManySheet(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        map.forEach((key, value) -> {
            if (value != null) {
                // 遍历JSONArray中的每个JSONObject
                for (int i = 0; i < value.size(); i++) {
                    JSONObject jsonObject = value.getJSONObject(i);
                    // 遍历JSONObject中的每个键值对，删除值为null或空字符串的属性
                    jsonObject.keySet().removeIf(key1 -> {
                        Object obj = jsonObject.get(key1);
                        return obj == null || obj.toString().trim().isEmpty();
                    });
                }
            }

            // 如果过滤后JSONArray不为空，才打印
            if (value != null && !value.isEmpty()) {
                System.out.println("Sheet名称：" + key);
                System.out.println("Sheet数据：" + value);
                System.out.println("----------------------");

                List<TestCaseExcelData> functionTreeExcelData = convertSheetData(value);

                // 保存数据到数据库
                log.info("分析后的数据："+functionTreeExcelData);

                saveDomainTestCaseExcelData(functionTreeExcelData,key,brandVehicleVersion,domainIndex);
            }
        });

    }

    private void saveDomainTestCaseExcelData(List<TestCaseExcelData> testCaseExcelData, String key,Map<String,String> brandVehicleVersion,Map<String,String> domainIndex) {

        List<DomainTestCaseEntity> domainTestCaseEntities = new ArrayList<>();

        testCaseExcelData.forEach(data -> {

            DomainTestCaseEntity caseEntity = new DomainTestCaseEntity();

            caseEntity.setId(snowflakeIdGenerator.nextId());

            if(Objects.nonNull(key)){
                String vehicleId = brandVehicleVersion.get(key);
                caseEntity.setVehicleId(Long.valueOf(vehicleId));
            }

            if (Objects.nonNull(data.getFunction_domain())&&Objects.nonNull(data.getThree_tag())){
                String domainId = domainIndex.get(data.getFunction_domain() + "-" + data.getThree_tag());
                if (Objects.isNull(domainId)){
                    System.out.println(data.getFunction_domain() + "-" + data.getThree_tag());
                }
                caseEntity.setDomainIndexId(Long.valueOf(domainId));
            }

            if(Objects.nonNull(data.getScore())){
                if (!data.getScore().replaceAll("\\s", "").equals("/")) {
                    caseEntity.setScore(Integer.valueOf(data.getScore()));
//                    caseEntity.setResponsePhotoName(Long.valueOf(brandVehicleVersion.get(key))+
//                            Long.valueOf(domainIndex.get(data.getFunction_domain() + "-" + data.getThree_tag()))+
//                            "");
                }
            }

            if (Objects.nonNull(data.getTest_case())){
                if (!data.getTest_case().replaceAll("\\s", "").equals("/")) {
                    caseEntity.setTestInstruct(data.getTest_case());
                }
            }

            if (Objects.nonNull(data.getScore_explain())){
                if (!data.getScore_explain().replaceAll("\\s", "").equals("/")) {
                    caseEntity.setInterpretationOfResult(data.getScore_explain());
                }
            }



            domainTestCaseEntities.add(caseEntity);
        });

        saveBatch(domainTestCaseEntities);

    }

    // 判断文件是否为图片
    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                fileName.endsWith(".bmp");
    }

    /**
     * 功能树
     * @param folder
     */
    public void processFolder(File folder,String vehicleId,Map<String,String> domainIndex) {
        // 获取文件夹中的所有文件和子文件夹
        File[] files = folder.listFiles((dir, name) ->
                !name.startsWith("~$") &&
                        !name.startsWith("._"));

        String httpResponse;

        if (files != null) {
            for (File file : files) {
                if (file.getPath().contains("测试评分说明图片")) {
                    if (file.isFile()) {
                        if (isImageFile(file)) {
                            log.info("图片文件: " + file.getName());
                            //TODO 上传到阿里云OSS
                            String[] split = file.getName().split("-");

                            String domainIndexId = domainIndex.get((split[split.length - 2] + "-" + split[split.length-1]).split("\\.")[0]);

                            if (domainIndexId != null) {

                                DomainTestCaseEntity one = getOne(new QueryWrapper<DomainTestCaseEntity>()
                                        .eq("vehicle_id", vehicleId)
                                        .eq("domain_index_id", domainIndexId));
                                long pictureName = snowflakeIdGenerator.nextId();

                                one.setResponsePhotoName(pictureName+"");
                                updateById(one);

                                if (Objects.nonNull(domainIndexId)){
                                    OssUploadRequest ossUploadRequest = new OssUploadRequest();
                                    ossUploadRequest.setFile_type("image");
                                    ossUploadRequest
                                            .setFile_id(pictureName+"");

                                    log.info(ossUploadRequest.getFile_id());
                                    RequestBody requestBody = new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("file_id", ossUploadRequest.getFile_id())
                                            .addFormDataPart("file_type", ossUploadRequest.getFile_type())
                                            .addFormDataPart("uploaded_file", file.getName(),
                                                    RequestBody.create(file, MediaType.parse("image/jpeg")))
                                            .build();

                                    // 构建请求
                                    Request request = new Request.Builder()
                                            .url(ossConfig.getSaveFileToOSS())
                                            .post(requestBody)
                                            .build();

                                    // 发送请求
                                    try (Response response = client.newCall(request).execute()) {
                                        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                                        // 打印响应
                                        System.out.println(response.body().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }else if (file.isDirectory()) {
//                    log.info("进入文件夹: " + file.getName());
                        // 递归处理子文件夹
                        processFolder(file, vehicleId,domainIndex);
                    }
                }else if (file.isDirectory()) {
//                    log.info("进入文件夹: " + file.getName());
                    // 递归处理子文件夹
                    processFolder(file, vehicleId,domainIndex);
                }
            }
        } else {
            log.info("文件夹为空: " + folder.getAbsolutePath());
        }
    }

    /**
     * 将JSONArray转换为List<FunctionTreeExcelData>对象 反序列化
     * @param sheetData
     * @return
     */
    public static List<TestCaseExcelData> convertSheetData(JSONArray sheetData) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TestCaseExcelData> records = objectMapper.convertValue(sheetData, new TypeReference<List<TestCaseExcelData>>() {});

        // 遍历每个记录进行字段转换
        for (TestCaseExcelData record : records) {
            // 将中文字段转换为英文
            if (record.getTest_date() != null) {
                record.setTest_date(record.getTest_date());
            }
            if (record.getTest_case() != null) {
                record.setTest_date(record.getTest_case());
            }
            if (record.getScore() != null) {
                record.setScore(record.getScore());
            }
            if (record.getVehicle_system_version() != null) {
                record.setVehicle_system_version(record.getVehicle_system_version());
            }
            if (record.getVehicle_model() != null) {
                // 车型拆分为品牌和型号
                String[] brandVehicleModel = record.getVehicle_model().split("-");
                if (brandVehicleModel.length > 0) {
                    record.setBrand(brandVehicleModel[0]);
                    if (brandVehicleModel.length > 1) {
                        record.setVehicle_model(brandVehicleModel[1]);
                    }
                }
            }
            if (record.getFunction_domain() != null) {
                record.setFunction_domain(record.getFunction_domain());
            }
            if (record.getThree_tag() != null) {
                record.setThree_tag(record.getThree_tag());
            }
            if (record.getScore_explain() != null) {
                record.setScore_explain(record.getScore_explain());
            }
        }
        return records;
    }
}
