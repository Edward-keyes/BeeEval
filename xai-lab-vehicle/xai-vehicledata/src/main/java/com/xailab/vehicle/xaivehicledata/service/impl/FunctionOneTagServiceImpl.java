package com.xailab.vehicle.xaivehicledata.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.druid.sql.visitor.functions.If;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xailab.vehicle.feign.vo.FunctionThreeTagVos;
import com.xailab.vehicle.xaicommon.utils.*;
import com.xailab.vehicle.xaivehicledata.config.ALiYunOssConfig;
import com.xailab.vehicle.xaivehicledata.config.ConstantConfig;
import com.xailab.vehicle.xaivehicledata.controller.ALiYunOSSController;
import com.xailab.vehicle.xaivehicledata.dao.FunctionDomainVideoDao;
import com.xailab.vehicle.xaivehicledata.dao.FunctionThreeTagDao;
import com.xailab.vehicle.xaivehicledata.entity.*;
import com.xailab.vehicle.xaivehicledata.entity.constant.VehicleConstant;
import com.xailab.vehicle.xaivehicledata.entity.request.*;
import com.xailab.vehicle.xaivehicledata.entity.response.FunctionTreeOneAndTwoTagResponse;
import com.xailab.vehicle.xaivehicledata.entity.response.OSSResponse;
import com.xailab.vehicle.xaivehicledata.entity.vo.*;
import com.xailab.vehicle.xaivehicledata.service.*;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.xailab.vehicle.xaivehicledata.dao.FunctionOneTagDao;


@Log4j2
@Service("functionOneTagService")
@RequiredArgsConstructor
public class FunctionOneTagServiceImpl extends ServiceImpl<FunctionOneTagDao, FunctionOneTagEntity> implements FunctionOneTagService {

    @Autowired
    private FunctionTwoTagService functionTwoTagService;

    @Autowired
    private FunctionThreeTagService functionThreeTagService;

    @Autowired
    private FunctionThreeTagDao functionThreeTagDao;

    @Autowired
    private BaseInfoService baseInfoService;

    @Autowired
    private FunctionTreeService functionTreeService;

    @Autowired
    private FunctionOneTagDao functionOneTagDao;

    @Resource
    private VehicleUserService vehicleUserService;

    @Resource
    private VehicleTryUserService vehicleTryUserService;

    @Resource
    private ALiYunOSSService aLiYunOSSService;

    @Resource
    private ALiYunOSSController aLiYunOSSController;

    @Resource
    private FunctionalDomainService functionalDomainService;

    @Resource
    private FunctionDomainVideoService functionDomainVideoService;

    @Resource
    private FunctionDomainVideoDao functionDomainVideoDao;

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
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FunctionOneTagEntity> page = this.page(
                new Query<FunctionOneTagEntity>().getPage(params),
                new QueryWrapper<FunctionOneTagEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void inputFunctionTree(MultipartFile file) {

        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(0, 0);

        List<FunctionTreeInputVo> users = null;
        try {
            users = ExcelUtils.readMultipartFile(file, FunctionTreeInputVo.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Map<String, Map<String, Map<String,String>>> functionTree =
                users.stream()
                        .collect(
                            Collectors.groupingBy(FunctionTreeInputVo::getFirstLevelLabel,
                            LinkedHashMap::new,
                                Collectors.groupingBy(FunctionTreeInputVo::getSecondLevelLabel,
                                     LinkedHashMap::new,
                                     Collectors.toMap(FunctionTreeInputVo::getThirdLevelLabel,
                                             FunctionTreeInputVo::getDescription
                                     )
                                )
                            )
                        );

        for (String firstLevelLabel : functionTree.keySet()) {
            Map<String, Map<String,String>> secondLevelMap = functionTree.get(firstLevelLabel);
            System.out.println(firstLevelLabel);

            FunctionOneTagEntity functionOneTagEntity = new FunctionOneTagEntity();
            functionOneTagEntity.setId(idWorker.nextId());
            functionOneTagEntity.setTagName(firstLevelLabel);
            save(functionOneTagEntity);

            for (String secondLevelLabel : secondLevelMap.keySet()) {
                Map<String, String> thirdLevelMap = secondLevelMap.get(secondLevelLabel);
                System.out.println("\t" + secondLevelLabel);

                FunctionTwoTagEntity functionTwoTagEntity = new FunctionTwoTagEntity();
                functionTwoTagEntity.setId(idWorker.nextId());
                functionTwoTagEntity.setTagName(secondLevelLabel);
                functionTwoTagEntity.setFunctionOneTagId(functionOneTagEntity.getId());
                functionTwoTagService.save(functionTwoTagEntity);

                for (String thirdLevelLabel : thirdLevelMap.keySet()) {
                    String description = thirdLevelMap.get(thirdLevelLabel);
                    System.out.println("\t\t" + thirdLevelLabel + " : " + description);

                    FunctionThreeTagEntity functionThreeTagEntity = new FunctionThreeTagEntity();
                    functionThreeTagEntity.setId(idWorker.nextId());
                    String[] split = thirdLevelLabel.split("-");
                    functionThreeTagEntity.setTagNumber(split[0]);
                    functionThreeTagEntity.setTagName(split[1]);
                    functionThreeTagEntity.setDescription(description);
                    functionThreeTagEntity.setFunctionTwoTagId(functionTwoTagEntity.getId());
                    functionThreeTagService.save(functionThreeTagEntity);

                }
            }
        }

    }


    public String splitString(String str) {
        int lastIndex = str.lastIndexOf('\\');

        String result = str.substring(lastIndex+1, str.length());

        return result;
    }

    @Override
    public void functionTreeData(String path) {

        Map<String,Long> functionThreeTagMap =functionThreeTagService.getAllIdAndTagNumber();

        File folder = new File(path);

        String[] excelName = path.split("-");
        String version = path.substring(path.indexOf("-")+1, path.lastIndexOf("-"));
        Long vehicleId =
                baseInfoService.getVehicleIdByBMV(splitString(excelName[0]), version, excelName[excelName.length-1]);


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
//                            ReadExcel(cMultiFile,vehicleId,functionThreeTagMap);
                        }
                    } else if (file.isDirectory()) {
                        log.info("文件夹: " + file.getName());
                        processFolder(file,vehicleId);
                    }
                }
            } else {
                log.info("文件夹为空");
            }
        } else {
            log.info("指定路径不是一个文件夹");
        }

    }

    /**
     * 功能树
     * @param folder
     * @param vehicleId
     */
    public void processFolder(File folder,Long vehicleId) {
        // 获取文件夹中的所有文件和子文件夹
        File[] files = folder.listFiles((dir, name) ->
                !name.startsWith("~$") &&
                        !name.startsWith("._"));

        String httpResponse;

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    // 如果是图片文件，输出图片名称
                    if (isImageFile(file)) {

                        log.info("图片文件: " + file.getName());
                        //TODO 上传到阿里云OSS
                        OssUploadRequest ossUploadRequest = new OssUploadRequest();
                        ossUploadRequest.setFile_type("image");
                        ossUploadRequest
                                .setFile_id(vehicleId
                                        +"-"
                                        +splitString(
                                        file.getName()
                                                .substring(0,
                                                        file.getName().lastIndexOf('.'))));
                        ossUploadRequest.setLocal_file_path(file.getAbsolutePath());

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
                    // 如果是视频文件，输出视频名称
                    else if (isVideoFile(file)) {
                        /**
                        log.info("视频文件: " + file.getName());

                        OssUploadRequest ossUploadRequest = new OssUploadRequest();
                        ossUploadRequest.setFile_type("video");
                        ossUploadRequest
                                .setFile_id(vehicleId
                                        +"-"
                                        +splitString(
                                        file.getName()
                                                .substring(0,
                                                        file.getName().lastIndexOf('.'))));
                        ossUploadRequest.setLocal_file_path(file.getAbsolutePath());

                        log.info(ossUploadRequest.getFile_id());


                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("file_id", ossUploadRequest.getFile_id())
                                .addFormDataPart("file_type", ossUploadRequest.getFile_type())
                                .addFormDataPart("uploaded_file", file.getName(),
                                        RequestBody.create(file, MediaType.parse("video/mp4")))
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

                        */
                    }

                    //如果是字幕文件
                    else if(isStrFile(file) &&
                            file.getName().split("-")[file.getName().split("-").length-1]
                                    .equals("en.srt")){
                        log.info("字幕文件: " + file.getName());
                        MultipartFile cMultiFile = fileConvertMultipartFile(file);
                        try {
                            String s = aLiYunOSSController.uploadVideoStr(cMultiFile, vehicleId + "-" + file.getName());
                            if ("上传成功".equals(s)){
                                FunctionTreeEntity one = functionTreeService.getOne(
                                        new QueryWrapper<FunctionTreeEntity>()
                                                .eq("vehicle_id", vehicleId)
                                                .eq("video_number", file.getName().split("-")[0]));
                                if (Objects.nonNull(one)) {
                                    one.setVideoStr(1);
                                    functionTreeService.updateById(one);
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                } else if (file.isDirectory()) {
//                    log.info("进入文件夹: " + file.getName());
                    // 递归处理子文件夹
                    processFolder(file,vehicleId);
                }
            }
        } else {
            log.info("文件夹为空: " + folder.getAbsolutePath());
        }
    }



    // 判断文件是否为图片
    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                fileName.endsWith(".bmp");
    }

    // 判断文件是否为视频
    private boolean isVideoFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".mp4") || fileName.endsWith(".avi") ||
                fileName.endsWith(".mov") || fileName.endsWith(".mkv") ||
                fileName.endsWith(".flv");
    }

    // 判断文件是否为字幕文件
    private boolean isStrFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".srt");
    }

    @Override
    public List<FunctionDomainResultVo> queryAllOneTagCountThreeTag(String language) {
        List<FunctionalDomainVo> functionalDomainVos = functionOneTagDao.queryAllOneTagCountThreeTag(language);

        // 按 oneId 分组
        Map<String, List<FunctionalDomainVo>> groupedByOneId = functionalDomainVos.stream()
                .collect(Collectors.groupingBy(FunctionalDomainVo::getOneId));

        // 构建 FunctionDomainResultVo 列表
        List<FunctionDomainResultVo> result = new ArrayList<>();
        for (Map.Entry<String, List<FunctionalDomainVo>> entry : groupedByOneId.entrySet()) {
            String oneId = entry.getKey();
            List<FunctionalDomainVo> group = entry.getValue();

            // 创建 FunctionDomainResultVo
            FunctionDomainResultVo resultVo = new FunctionDomainResultVo();
            resultVo.setOneId(oneId);
            resultVo.setOneTagName(group.get(0).getOneTagName()); // 取第一个元素的 oneTagName

            // 构建 ThreeData 列表
            List<ThreeData> threeDatas = group.stream()
                    .map(vo -> new ThreeData(vo.getThreeId(), vo.getThreeTagName()))
                    .collect(Collectors.toList());
            resultVo.setThreeDatas(threeDatas);

            // 添加到结果列表
            result.add(resultVo);
        }

        return result;
    }

    @Override
    public List<HighlightFunctionRequest> queryAllHighlightFunction(String language) {

        List<HighlightFunctionVo> functionalDomainVos = functionOneTagDao.queryAllHighlightFunction(language);

        return ListHighlightFunctionVoConvertListHighlightFunctionRequest(functionalDomainVos,language);
    }

    @Override
    public List<FunctionOneTagVo> queryAllFunctionTagTree(String language) {

        List<FunctionTagVo> functionTagVos = functionOneTagDao.queryAllFunctionTag(language);

        // 外层 Map，key 为 OneTagName
        Map<String, FunctionOneTagVo> oneTagMap = new HashMap<>();

        for (FunctionTagVo vo : functionTagVos) {
            // 获取或创建 FunctionOneTagVo
            FunctionOneTagVo oneTagVo = oneTagMap.computeIfAbsent(vo.getOneTagName(), k -> {
                FunctionOneTagVo newOneTagVo = new FunctionOneTagVo();
                newOneTagVo.setOneTagName(vo.getOneTagName());
                newOneTagVo.setFunctionTwoTagVOList(new ArrayList<>());
                return newOneTagVo;
            });

            // 获取或创建 FunctionTwoTagVo
            List<FunctionTwoTagVo> twoTagVoList = oneTagVo.getFunctionTwoTagVOList();
            FunctionTwoTagVo twoTagVo = findOrCreateTwoTagVo(twoTagVoList, vo.getTwoTagName());

            // 创建 FunctionThreeTagVo 并添加到 FunctionTwoTagVo
            FunctionThreeTagVo threeTagVo = new FunctionThreeTagVo(vo.getThreeId(), vo.getThreeTagName());
            twoTagVo.getFunctionThreeTagVOList().add(threeTagVo);
        }

        return new ArrayList<>(oneTagMap.values());
    }

    @Override
    public FunctionTreeVideoRequest queryVideoByThreeTagIdAndVehicleId(QueryFunctionTreeVideoRequest request) {
        FunctionTreeVideoRequest functionTreeVideoRequest = new FunctionTreeVideoRequest();
        FunctionTreeVideoVo functionTreeVideoVo = functionOneTagDao.queryVideoByThreeTagIdAndVehicleId(request.getVehicleId(), request.getThreeTagId(),request.getLanguage());
        if(functionTreeVideoVo.getDescription()!=null) {
            functionTreeVideoRequest.setDescription(functionTreeVideoVo.getDescription());
        }
        if(functionTreeVideoVo.getVideoNumber()!=null) {
            functionTreeVideoRequest.setVideoId(request.getVehicleId() + "-"+functionTreeVideoVo.getVideoNumber());
            Okhttp3Utils okhttp3Utils = new Okhttp3Utils();
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            try {
                String data = okhttp3Utils.getData(ossConfig.getGetPlayList() + "?file_id=" + request.getVehicleId() + "-" + functionTreeVideoVo.getVideoNumber(), headers);
                OSSResponse ossResponse = JsonUtils.jsonToObj(data, OSSResponse.class);
                functionTreeVideoRequest.setVideoNumber(ossResponse.getPlaylist_url());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (functionTreeVideoVo.getFunctionLabel()!= null) {
            functionTreeVideoRequest.setFunctionLabel(functionTreeVideoVo.getFunctionLabel().split("-"));
        }
        if(Objects.nonNull(functionTreeVideoVo.getVideoStr())&&functionTreeVideoVo.getVideoStr()==1&&"en".equals(request.getLanguage())) {
            functionTreeVideoRequest.setVideoStr(aLiYunOSSService.queryStr(request.getVehicleId() + "-" + functionTreeVideoVo.getVideoNumber() + "-en.srt"));
        }
        return functionTreeVideoRequest;
    }

    @Override
    public FunctionTreeVideoNewRequest queryVideoByThreeTagIdAndVehicleIdNew(QueryFunctionTreeVideoRequest functionTreeVideoRequest) {
        List<FunctionTreeVideoNewVo> functionTreeVideoVo = functionThreeTagDao.queryCaseVideo(functionTreeVideoRequest.getThreeTagId(), functionTreeVideoRequest.getVehicleId());
        return convert(functionTreeVideoVo);
    }

    @Override
    public FunctionTreeVideoNewRequest convert(List<FunctionTreeVideoNewVo> voList) {

        Okhttp3Utils okhttp3Utils = new Okhttp3Utils();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        FunctionTreeVideoNewRequest request = new FunctionTreeVideoNewRequest();

        // 1. 处理 description（取第一个非空值）
        String description = voList.stream()
                .map(FunctionTreeVideoNewVo::getDescription)
                .filter(d -> d != null && !d.isEmpty())
                .findFirst()
                .orElse(null);
        request.setDescription(description);

        // 2. 处理 functionLabel（拆分为数组并去重）
        String functionLabel = voList.stream()
                .map(FunctionTreeVideoNewVo::getFunctionLabel)
                .filter(d -> d != null && !d.isEmpty())
                .findFirst()
                .orElse(null);

        // 3. 处理 caseDataList（每个Vo转换为FunctionCaseData）
        List<FunctionCaseData> caseDataList = new ArrayList<>();
        for (FunctionTreeVideoNewVo vo : voList) {
            FunctionCaseData caseData = new FunctionCaseData();
            caseData.setCaseContent(vo.getCaseContent());
            if (Objects.nonNull(vo.getFunctionLabel())) {
                caseData.setFunctionLabel(vo.getFunctionLabel().split("-"));
            }
            if (Objects.nonNull(vo.getMaterialUrl())){
                try {
                    String data=null;
                    if (vo.getType()==0) {
                        // 0是图片
                        data= okhttp3Utils.getData(ossConfig.getGetImageUrl() + "?file_id=" + vo.getMaterialUrl(), headers);
                        OSSResponse ossResponse = JsonUtils.jsonToObj(data, OSSResponse.class);
                        caseData.setVideoNumber(ossResponse.getUrl());
                    }else if (vo.getType()==1){
                        // 1是视频
                        data = okhttp3Utils.getData(ossConfig.getGetPlayList() + "?file_id=" + vo.getMaterialUrl(), headers);
                        OSSResponse ossResponse = JsonUtils.jsonToObj(data, OSSResponse.class);
                        caseData.setVideoNumber(ossResponse.getPlaylist_url());
                    }

                    caseData.setMaterialType(vo.getType());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            caseData.setVideoStr(null); // 无数据来源，设为null
            if (Objects.nonNull(caseData.getVideoNumber())) {
                caseDataList.add(caseData);
            }
        }
        request.setCaseDataList(caseDataList);

        return request;
    }

    @Override
    public List<FunctionTreeCompareRequest> queryOtherVideoByThreeTagId(String threeTagId,String vehicleId,String language) {

        VehicleConstant constant=new VehicleConstant();
        Object loginId = StpUtil.getTokenInfo().getLoginId();
        VehicleUserEntity user = vehicleUserService.getById(loginId+"");

        List<String> vehicleIds = new ArrayList<>();


        if (user.getStatus() == 1){
            List<VehicleTryUserEntity> vehicleTryUserEntityList = vehicleTryUserService.getTryUserListByUserId(Long.parseLong(loginId + ""));
            if (!vehicleTryUserEntityList.isEmpty()) {
                for (VehicleTryUserEntity tryUserEntity : vehicleTryUserEntityList) {
                    vehicleIds.add(tryUserEntity.getVehicleId()+"");
                }
            }else {
                vehicleIds.add(constant.getVehicle2Id());
                vehicleIds.add(constant.getVehicle1Id());
            }
        }

        List<FunctionTreeCompareRequest> functionTreeVideoRequests = new ArrayList<>();
        List<FunctionTreeCompareVehicleVo> functionTreeVideoVos = functionOneTagDao.queryOtherVideoByThreeTagId(threeTagId, vehicleId,vehicleIds, language);
        for (FunctionTreeCompareVehicleVo functionTreeVideoVo : functionTreeVideoVos) {
            FunctionTreeCompareRequest functionTreeVideoRequest = new FunctionTreeCompareRequest();
            if (functionTreeVideoVo.getDescription()!= null) {
                functionTreeVideoRequest.setDescription(functionTreeVideoVo.getDescription());
            }
            if (functionTreeVideoVo.getVideoNumber()!= null) {
                Okhttp3Utils okhttp3Utils = new Okhttp3Utils();
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                try {
                    String data = okhttp3Utils.getData(ossConfig.getGetPlayList() + "?file_id=" + functionTreeVideoVo.getVehicleId() + "-" + functionTreeVideoVo.getVideoNumber(), headers);
                    OSSResponse ossResponse = JsonUtils.jsonToObj(data, OSSResponse.class);
                    functionTreeVideoRequest.setVideoNumber(ossResponse.getPlaylist_url());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (functionTreeVideoVo.getFunctionLabel()!= null) {
                functionTreeVideoRequest.setFunctionLabel(functionTreeVideoVo.getFunctionLabel().split("-"));
            }
            if(Objects.nonNull(functionTreeVideoVo.getSrtUrl())&&Integer.parseInt(functionTreeVideoVo.getSrtUrl())==1&&"en".equals(language)) {
                functionTreeVideoRequest.setVideoStr(aLiYunOSSService.queryStr(functionTreeVideoVo.getVehicleId() + "-" + functionTreeVideoVo.getVideoNumber() + "-en.srt"));
            }
            functionTreeVideoRequest.setBrandModel(functionTreeVideoVo.getBrandModel());
            if (functionTreeVideoRequest.getVideoNumber()!=null) {
                functionTreeVideoRequests.add(functionTreeVideoRequest);
            }
        }

        return functionTreeVideoRequests;
    }

    @Override
    public List<FunctionTreeOneAndTwoTagResponse> getAllOneAndTwoTag() {

        List<FunctionTreeOneAndTwoTagVo> response=functionOneTagDao.getAllOneAndTwoTag();

        return convertWithStream(response);
    }



    // 生成一级标签的唯一标识key
    public List<FunctionTreeOneAndTwoTagResponse> convertWithStream(List<FunctionTreeOneAndTwoTagVo> voList) {
        // 创建中间收集器，按一级标签分组
        Map<String, List<FunctionTreeOneAndTwoTagVo>> groupedByOneTag = voList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        vo -> generateOneTagKey(vo)
                ));

        return groupedByOneTag.values().stream()
                .map(group -> {
                    // 获取第一项作为一级标签基础数据
                    FunctionTreeOneAndTwoTagVo first = group.get(0);
                    FunctionTreeOneAndTwoTagResponse response = new FunctionTreeOneAndTwoTagResponse();
                    response.setOneTagId(first.getOneTagId());
                    response.setOneTagNumber(first.getOneTagNumber());
                    response.setOneTagName(first.getOneTagName());

                    // 处理二级标签（包含三级标签分组）
                    // 1. 先按二级标签分组
                    Map<String, List<FunctionTreeOneAndTwoTagVo>> twoTagGroups = group.stream()
                            .filter(vo -> vo.getTwoTagId() != null)
                            .collect(Collectors.groupingBy(
                                    vo -> generateTwoTagKey(vo)  // 自定义二级分组键
                            ));

                    // 2. 构建二级标签列表（包含各自的三级标签）
                    List<FunctionTwoTagVos> twoTags = twoTagGroups.values().stream()
                            .map(twoGroup -> {
                                FunctionTreeOneAndTwoTagVo firstTwo = twoGroup.get(0);

                                // 创建二级标签对象
                                FunctionTwoTagVos twoTag = new FunctionTwoTagVos();
                                twoTag.setTwoTagId(firstTwo.getTwoTagId());
                                twoTag.setTwoTagNumber(firstTwo.getTwoTagNumber());
                                twoTag.setTwoTagName(firstTwo.getTwoTagName());

                                // 构建三级标签列表
                                List<FunctionThreeTagVos> threeTags = twoGroup.stream()
                                        .filter(vo -> vo.getThreeTagId() != null)  // 过滤有效三级标签
                                        .map(vo -> new FunctionThreeTagVos(
                                                vo.getThreeTagId(),
                                                vo.getThreeTagNumber(),
                                                vo.getThreeTagName()
                                        ))
                                        .distinct()  // 可选：根据业务需求去重
                                        .collect(Collectors.toList());

                                twoTag.setThreeTagVos(threeTags);
                                return twoTag;
                            })
                            .collect(Collectors.toList());

                    response.setTwoTagList(twoTags);
                    return response;
                })
                .collect(Collectors.toList());
    }

    // 生成一级标签分组键（保持不变）
    private String generateOneTagKey(FunctionTreeOneAndTwoTagVo vo) {
        return vo.getOneTagId() + "|" + vo.getOneTagNumber();
    }

    // 生成二级标签分组键（新增）
    private String generateTwoTagKey(FunctionTreeOneAndTwoTagVo vo) {
        return vo.getTwoTagId() + "|" + vo.getTwoTagNumber();
    }

    private static FunctionTwoTagVo findOrCreateTwoTagVo(List<FunctionTwoTagVo> twoTagVoList, String twoTagName) {
        for (FunctionTwoTagVo twoTagVo : twoTagVoList) {
            if (twoTagVo.getTwoTagName().equals(twoTagName)) {
                return twoTagVo;
            }
        }
        FunctionTwoTagVo newTwoTagVo = new FunctionTwoTagVo();
        newTwoTagVo.setTwoTagName(twoTagName);
        newTwoTagVo.setFunctionThreeTagVOList(new ArrayList<>());
        twoTagVoList.add(newTwoTagVo);
        return newTwoTagVo;
    }


    public List<HighlightFunctionRequest> ListHighlightFunctionVoConvertListHighlightFunctionRequest(List<HighlightFunctionVo> voList,String language) {
        Object loginId = StpUtil.getTokenInfo().getLoginId();
        VehicleUserEntity user;
        if (Objects.nonNull(loginId)) {
            user = vehicleUserService.getById(loginId+"");
        } else {
            user = null;
        }

        // 使用 Map 来临时存储数据，key 为 oneTagName
        Map<String, HighlightFunctionRequest> oneTagMap = new HashMap<>();
        ConstantConfig constantConfig = new ConstantConfig();
        VehicleConstant vehicleConstant = new VehicleConstant();
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (HighlightFunctionVo vo : voList) {
            executorService.submit(() -> {
                // 获取或创建 HighlightFunctionRequest
                HighlightFunctionRequest oneTagRequest = oneTagMap.computeIfAbsent(vo.getOneTagName(), k -> new HighlightFunctionRequest(vo.getOneTagName(), vo.getTagIcon(), new ArrayList<>()));
                oneTagRequest.setIconUrl(vo.getTagIcon());
                // 获取或创建 TwoTagHighlightRequest
                List<TwoTagHighlightRequest> twoTagRequests = oneTagRequest.getTwoTagHighlightRequests();
                TwoTagHighlightRequest twoTagRequest = findOrCreateTwoTagRequest(twoTagRequests, vo.getTwoTagName());

                Okhttp3Utils okhttp3Utils = new Okhttp3Utils();
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                String url = "";
                try {
                    String data = okhttp3Utils.getData(ossConfig.getGetPlayList() + "?file_id=" + vo.getVehicleId() + "-" + vo.getVideoNumber(), headers);
                    OSSResponse ossResponse = JsonUtils.jsonToObj(data, OSSResponse.class);
                    url = ossResponse.getPlaylist_url();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (Objects.nonNull(user)&&user.getStatus() == 1) {
                    Map<String,Long> tryMap = new HashMap<>();
                    List<VehicleTryUserEntity> vehicleTryUserEntityList = vehicleTryUserService.getTryUserListByUserId(Long.parseLong(loginId + ""));
                    if (!vehicleTryUserEntityList.isEmpty()){
                        vehicleTryUserEntityList.stream().forEach(vehicleTryUserEntity -> {
                            tryMap.put(vehicleTryUserEntity.getVehicleId()+"",vehicleTryUserEntity.getUserId());
                        });
                        if (tryMap.get(vo.getVehicleId()) != null){
                            vo.setStatus(3);
                        }
                    }else {
                        if (vehicleConstant.getVehicle1Id().equals(vo.getVehicleId()) || vehicleConstant.getVehicle2Id().equals(vo.getVehicleId())) {
                            vo.setStatus(3);
                        }
                    }
                }

                if(Objects.nonNull(vo.getSrtUrl())&&Integer.valueOf(vo.getSrtUrl())==1&&"en".equals(language)) {
                    vo.setSrtUrl(aLiYunOSSService.queryStr(vo.getVehicleId() + "-" + vo.getVideoNumber() + "-en.srt"));
                }

                // 创建并添加 ThreeTagHighlightRequest
                ThreeTagHighlightRequest threeTagRequest = new ThreeTagHighlightRequest(vo.getThreeTagName(), url, vo.getBrandModel(),vo.getVehicleId(),vo.getStatus(),vo.getSrtUrl());
                twoTagRequest.getThreeTagHighlightRequests().add(threeTagRequest);
            });
        }

        executorService.shutdown(); // 关闭线程池
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // 等待所有任务完成
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<HighlightFunctionRequest> result = new ArrayList<>();
        result.addAll(oneTagMap.values());
        // 将 Map 转换为 List
        return result;
    }

    private TwoTagHighlightRequest findOrCreateTwoTagRequest(List<TwoTagHighlightRequest> twoTagRequests, String twoTagName) {
        for (TwoTagHighlightRequest request : twoTagRequests) {
            if (request.getTwoTagName().equals(twoTagName)) {
                return request;
            }
        }
        TwoTagHighlightRequest newRequest = new TwoTagHighlightRequest(twoTagName, new ArrayList<>());
        twoTagRequests.add(newRequest);
        return newRequest;
    }

    /**
     * 读取Excel不同sheet中的数据文件并保存到数据库
     * @param file
     */
    public void ReadExcel(MultipartFile file, Long vehicleId,Map<String,Long> functionThreeTagMap){

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

                List<FunctionTreeExcelData> functionTreeExcelData = convertSheetData(value);

                // 保存数据到数据库
                log.info("分析后的数据："+functionTreeExcelData);

                saveFunctionTreeByTreeExcelData(functionTreeExcelData,vehicleId,functionThreeTagMap,key);
            }
        });

    }

    /**
     * 读取Excel不同sheet中的数据文件并保存到数据库
     * @param file
     */
    public void ReadExcel2(MultipartFile file, Long vehicleId,Map<String, List<FunctionDomainIndexMapVo>> functionDomainData){

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

                List<FunctionDomainVideoEntity> functionTreeExcelData = convertSheetNewData(value,vehicleId,functionDomainData);

                // 保存数据到数据库
                log.info("分析后的数据："+functionTreeExcelData);

                functionDomainVideoService.saveBatch(functionTreeExcelData);
            }
        });

    }

    /**
     * 保存数据到数据库
     * 根据FunctionTreeExcelData对象
     * @param functionTreeExcelData
     */
    private void saveFunctionTreeByTreeExcelData(List<FunctionTreeExcelData> functionTreeExcelData, Long vehicleId,Map<String,Long> functionThreeTagMap,String sheetName) {

        List<FunctionTreeEntity> functionTreeEntities = new ArrayList<>();

        functionTreeExcelData.forEach(data -> {
            if (Objects.nonNull(data.getFunction_three_tag())) {
                FunctionTreeEntity functionTreeEntity = new FunctionTreeEntity();
                functionTreeEntity.setId(snowflakeIdGenerator.nextId());
                functionTreeEntity.setVehicleId(vehicleId);
                Long threeTagId = functionThreeTagMap.get(data.getFunction_three_tag().split("-")[0]);
                functionTreeEntity.setFunctionThreeTagId(threeTagId);

                if (sheetName.equals("A 感知能力")) {
                    functionTreeEntity.setFunctionList(isFunctionListG(data.getFunction_list()));
                } else {
                    functionTreeEntity.setFunctionList(isFunctionList(data.getFunction_list()));
                }
                if (Objects.nonNull(data.getLabel_explain())) {
                    if (!data.getLabel_explain().replaceAll("\\s", "").equals("/")) {
                        functionTreeEntity.setLabelExplain(isLabelExplain(data.getLabel_explain()));
                    }
                }
                if (Objects.nonNull(data.getFunction_label())) {
                    if (!data.getFunction_label().replaceAll("\\s", "").equals("/")) {
                        functionTreeEntity.setFunctionLabel(disposeFunctionLabel(data.getFunction_label()));
                    }
                }
                if (Objects.nonNull(data.getVideo_picture_number())) {
                    if (!data.getVideo_picture_number().replaceAll("\\s", "").equals("/")) {
                        if (data.getVideo_picture_number().contains("-")) {
                            functionTreeEntity.setPictureNumber(data.getVideo_picture_number());
                        } else {
                            functionTreeEntity.setVideoNumber(data.getVideo_picture_number());
                        }
                    }
                }
                functionTreeEntity.setTestDate(disposeTestDate(data.getTest_date()));
                functionTreeEntity.setVehicleSystemVersion(data.getVehicle_system_version());

                log.info("functionTree数据" + functionTreeEntity);
                functionTreeEntities.add(functionTreeEntity);
            }
        });

        // 批量保存
        functionTreeService.saveBatch(functionTreeEntities);

    }

    private static Date disposeTestDate(String testDate) {

        // Excel的基准日期是1899年12月30日
        LocalDate baseDate = LocalDate.of(1899, 12, 30);

        // 计算实际日期
        LocalDate actualDate = baseDate.plusDays(Long.parseLong(testDate));

        return Date.from(actualDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     *
     * @param functionLabel
     * @return
     */
    private static String disposeFunctionLabel(String functionLabel) {
        return functionLabel.substring(1).replace("\n", " ");
    }

    public static Integer isLabelExplain(String type){
        if (type.equals("问题")){
            return 0;
        }else if (type.equals("亮点")){
            return 1;
        }
        return 2;
    }

    public static Integer isFunctionList(String type){
        if (type.equals("Avg.")){
            return 2;
        }else if (type.equals("n.a.")){
            return 4;
        }else if (type.equals("Good")){
            return 1;
        }else if (type.equals("Poor")){
            return 3;
        }
        return 0;
    }

    public static Integer isFunctionListG(String type){
        if (type.equals("有")) {
            return 2;
        }
        return 0;
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
     * 将JSONArray转换为List<FunctionTreeExcelData>对象 反序列化
     * @param sheetData
     * @return
     */
    public static List<FunctionTreeExcelData> convertSheetData(JSONArray sheetData) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<FunctionTreeExcelData> records = objectMapper.convertValue(sheetData, new TypeReference<List<FunctionTreeExcelData>>() {});

        // 遍历每个记录进行字段转换
        for (FunctionTreeExcelData record : records) {
            // 将中文字段转换为英文
            if (record.getTest_date() != null) {
                record.setTest_date(record.getTest_date());
            }
            if (record.getLabel_explain() != null) {
                record.setLabel_explain(record.getLabel_explain());
            }
            if (record.getVideo_picture_number() != null) {
                record.setVideo_picture_number(record.getVideo_picture_number());
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
            if (record.getFunction_label() != null) {
                record.setFunction_label(record.getFunction_label());
            }
            if (record.getFunction_three_tag() != null) {
                record.setFunction_three_tag(record.getFunction_three_tag());
            }
            if (record.getFunction_list() != null) {
                record.setFunction_list(record.getFunction_list());
            }
        }
        return records;
    }

    public static List<FunctionDomainVideoEntity> convertSheetNewData(JSONArray sheetData,Long vehicleId,Map<String, List<FunctionDomainIndexMapVo>> functionDomainData) {
        List<FunctionDomainConvertVo> records = parseData(sheetData);
        List<FunctionDomainVideoEntity> functionDomainVideoEntities = new ArrayList<>();
        // 遍历每个记录进行字段转换
        for (FunctionDomainConvertVo record : records) {
            FunctionDomainVideoEntity functionDomainVideoEntity = new FunctionDomainVideoEntity();
            // 将中文字段转换为英文
            if (record.getFunctionDomainIdS() != null) {
                List<FunctionDomainIndexMapVo> functionDomainIndexMapVos = functionDomainData.get(record.getFunctionDomainIdS());
                if (record.getFunctionDomainIndexIdS() != null) {
                    for (FunctionDomainIndexMapVo functionDomainIndexMapVo : functionDomainIndexMapVos) {
                        if (functionDomainIndexMapVo.getIndexName().equals(record.getFunctionDomainIndexIdS())){
                            functionDomainVideoEntity.setFunctionDomainIndexId(functionDomainIndexMapVo.getDomainIndexId());
                            functionDomainVideoEntity.setFunctionDomainId(functionDomainIndexMapVo.getFunctionalId());
                        }
                    }
                }
            }

            if (record.getTitle() != null) {
                functionDomainVideoEntity.setTitle(record.getTitle());
            }
            if (record.getTaskType() != null) {
                functionDomainVideoEntity.setTaskType(record.getTaskType());
            }
            if (record.getDescription() != null) {
                functionDomainVideoEntity.setDescription(record.getDescription());
            }
            if (record.getVideoName()!=null){
                functionDomainVideoEntity.setType((record.getVideoName().charAt(0)+"").equals("G")?1:0);
            }
            if (record.getVideoName()!=null){
                functionDomainVideoEntity.setFileName(record.getVideoName());
            }
            if (record.getTitleEn()!=null){
                functionDomainVideoEntity.setTitleEn(record.getTitleEn());
            }
            if (record.getDescriptionEn()!=null) {
                functionDomainVideoEntity.setDescriptionEn(record.getDescriptionEn());
            }
            functionDomainVideoEntity.setVehicleId(vehicleId);
            functionDomainVideoEntity.setFileType(0);
            functionDomainVideoEntity.setUrlName(snowflakeIdGenerator.nextId()+"");
            functionDomainVideoEntities.add(functionDomainVideoEntity);
        }
        System.out.println("处理完成");
        return functionDomainVideoEntities;
    }

    public static List<FunctionDomainConvertVo> parseData(Object sheetData) {
        ObjectMapper objectMapper = new ObjectMapper();
        // 动态类型处理
        if (sheetData instanceof List) {
            return objectMapper.convertValue(sheetData, new TypeReference<List<FunctionDomainConvertVo>>() {});
        } else {
            FunctionDomainConvertVo vo = objectMapper.convertValue(sheetData, FunctionDomainConvertVo.class);
            return Collections.singletonList(vo);
        }
    }

    @Override
    public void functionDomainData(String path) {

        Map<String,Long> functionThreeTagMap =functionThreeTagService.getAllIdAndTagNumber();

        File folder = new File(path);

        String[] excelName = path.split("-");
        String version = path.substring(path.indexOf("-")+1, path.lastIndexOf("-"));
        Long vehicleId =
                baseInfoService.getVehicleIdByBMV(splitString(excelName[0]),version, excelName[excelName.length-1]);


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
                            ReadExcel(cMultiFile,vehicleId,functionThreeTagMap);
                        }
                    } else if (file.isDirectory()) {
                        log.info("文件夹: " + file.getName());
                        processFolderDomain(file,vehicleId);
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
    public void functionDomainNewData(String path) {

//        Map<String,Long> functionThreeTagMap =functionThreeTagService.getAllIdAndTagNumber();
        //获取所有功能域下，不同指标的id和名称
        Map<String, List<FunctionDomainIndexMapVo>> functionDomainData=functionalDomainService.queryAllFunctionDomainData();

        File folder = new File(path);

        String[] excelName = path.split("-");
        String version = path.substring(path.indexOf("-")+1, path.lastIndexOf("-"));
        Long vehicleId =
                baseInfoService.getVehicleIdByBMV(splitString(excelName[0]),version, excelName[excelName.length-1]);


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
                            ReadExcel2(cMultiFile,vehicleId,functionDomainData);
                        }
                    }
//                    else if (file.isDirectory()) {
//                        log.info("文件夹: " + file.getName());
//                        processFolderDomain(file,vehicleId);
//                    }
                }
            } else {
                log.info("文件夹为空");
            }
            List<FunctionDomainVideoEntity> functionDomainVideoEntityList = functionDomainVideoDao.selectList(Wrappers.<FunctionDomainVideoEntity>lambdaQuery()
                    .eq(FunctionDomainVideoEntity::getVehicleId, vehicleId));
            Map<String, Long> collect = functionDomainVideoEntityList.stream().collect(Collectors.toMap(FunctionDomainVideoEntity::getFileName, FunctionDomainVideoEntity::getId));
            if (files != null) {
                for (File file : files) {
                    if (isVideoFile(file)) {
                        if (Objects.nonNull(collect.get(file.getName().substring(0,file.getName().lastIndexOf("."))))) {
                            log.info("视频文件: " + file.getName());
                            OssUploadRequest ossUploadRequest = new OssUploadRequest();
                            ossUploadRequest.setFile_type("video");
                            ossUploadRequest.setFile_id(collect.get(file.getName().substring(0,file.getName().lastIndexOf(".")))+"");
                            ossUploadRequest.setLocal_file_path(file.getAbsolutePath());

                            log.info(ossUploadRequest.getFile_id());


                            RequestBody requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("file_id", ossUploadRequest.getFile_id())
                                    .addFormDataPart("file_type", ossUploadRequest.getFile_type())
                                    .addFormDataPart("uploaded_file", file.getName(),
                                            RequestBody.create(file, MediaType.parse("video/mp4")))
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
            } else {
                log.info("文件夹为空");
            }
        } else {
            log.info("指定路径不是一个文件夹");
        }

    }

    /**
     * 功能域
     * @param folder
     * @param vehicleId
     */
    public void processFolderDomain(File folder,Long vehicleId) {
        // 获取文件夹中的所有文件和子文件夹
        File[] files = folder.listFiles((dir, name) ->
                !name.startsWith("~$") &&
                        !name.startsWith("._"));

        String httpResponse;

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    // 如果是图片文件，输出图片名称
                    if (isImageFile(file)) {
                        log.info("图片文件: " + file.getName());
                        //TODO 上传到阿里云OSS
                        OssUploadRequest ossUploadRequest = new OssUploadRequest();
                        ossUploadRequest.setFile_type("image");
                        ossUploadRequest
                                .setFile_id(vehicleId
                                        +"-"
                                        +file.getName().charAt(0)
                                        +splitString(
                                        file.getName()
                                                .substring(0,
                                                        file.getName().indexOf('-'))));
                        ossUploadRequest.setLocal_file_path(file.getAbsolutePath());

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
                    // 如果是视频文件，输出视频名称
                    else if (isVideoFile(file)) {
                        log.info("视频文件: " + file.getName());
                        OssUploadRequest ossUploadRequest = new OssUploadRequest();
                        ossUploadRequest.setFile_type("video");
                        ossUploadRequest
                                .setFile_id(vehicleId
                                        +"-"
                                        +file.getName().charAt(0)
                                        +splitString(
                                        file.getName()
                                                .substring(0,
                                                        file.getName().lastIndexOf('.'))));
                        ossUploadRequest.setLocal_file_path(file.getAbsolutePath());

                        log.info(ossUploadRequest.getFile_id());


                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("file_id", ossUploadRequest.getFile_id())
                                .addFormDataPart("file_type", ossUploadRequest.getFile_type())
                                .addFormDataPart("uploaded_file", file.getName(),
                                        RequestBody.create(file, MediaType.parse("video/mp4")))
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
                } else if (file.isDirectory()) {
//                    log.info("进入文件夹: " + file.getName());
                    // 递归处理子文件夹
                    processFolderDomain(file,vehicleId);
                }
            }
        } else {
            log.info("文件夹为空: " + folder.getAbsolutePath());
        }
    }

    /**
     * 功能域
     * @param folder
     */
    public void processFolderNewDomain(File folder,Map<String, Long> collect) {
        // 获取文件夹中的所有文件和子文件夹
        File[] files = folder.listFiles((dir, name) ->
                !name.startsWith("~$") &&
                        !name.startsWith("._"));

        String httpResponse;

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    // 如果是视频文件，输出视频名称

                } else if (file.isDirectory()) {
//                    log.info("进入文件夹: " + file.getName());
                    // 递归处理子文件夹
                    processFolderNewDomain(file,collect);
                }
            }
        } else {
            log.info("文件夹为空: " + folder.getAbsolutePath());
        }
    }
}