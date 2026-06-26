package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaicommon.utils.SnowflakeIdGenerator;
import com.xailab.vehicle.xaivehicledata.config.ALiYunOssConfig;
import com.xailab.vehicle.xaivehicledata.controller.ALiYunOSSController;
import com.xailab.vehicle.xaivehicledata.dao.FunctionDomainVideoDao;
import com.xailab.vehicle.xaivehicledata.entity.*;
import com.xailab.vehicle.xaivehicledata.entity.request.OssUploadRequest;
import com.xailab.vehicle.xaivehicledata.entity.vo.VideoNumberVo;
import com.xailab.vehicle.xaivehicledata.service.BaseInfoService;
import com.xailab.vehicle.xaivehicledata.service.DomainTestCaseService;
import com.xailab.vehicle.xaivehicledata.service.FunctionDomainVideoService;
import com.xailab.vehicle.xaivehicledata.service.FunctionalDomainService;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.xailab.vehicle.xaivehicledata.service.impl.FunctionOneTagServiceImpl.fileConvertMultipartFile;

@Service("functionDomainVideoService")
@Slf4j
@RequiredArgsConstructor
public class FunctionDomainVideoServiceImpl extends ServiceImpl<FunctionDomainVideoDao, FunctionDomainVideoEntity> implements FunctionDomainVideoService {

    private final ALiYunOssConfig ossConfig;

    @Autowired
    private FunctionalDomainService functionalDomainService;

    @Autowired
    private BaseInfoService baseInfoService;

    @Autowired
    private DomainTestCaseService domainTestCaseService;

    @Autowired
    private FunctionDomainVideoDao functionDomainVideoDao;

    @Resource
    private ALiYunOSSController aLiYunOSSController;

    // 创建 OkHttpClient 实例
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(50000, TimeUnit.SECONDS) // 连接超时时间
            .writeTimeout(50000, TimeUnit.SECONDS)  // 写入超时时间
            .readTimeout(50000, TimeUnit.SECONDS)   // 读取超时时间
            .build();

    private static final SnowflakeIdGenerator snowflakeIdGenerator
            = new SnowflakeIdGenerator(0, 0);

    @Override
    public void uploadVideoAndPicture(String path) {

        /**
         * 获取所有功能域id
         */
        Map<String, Long> functionalDomainEntities = functionalDomainService
                .list().stream().collect(
                        Collectors.toMap(
                                FunctionalDomainEntity::getFunctionalDomainName,
                                FunctionalDomainEntity::getId));

//        functionalDomainEntities.get("功能域1");

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
                    if (file.isDirectory()) {
                        log.info("文件夹: " + file.getName());
                        processFolderDomain(file,vehicleId,functionalDomainEntities);
                    }
                }
            } else {
                log.info("文件夹为空");
            }
        } else {
            log.info("指定路径不是一个文件夹");
        }

    }

    public void processFolderDomain(File folder,Long vehicleId,Map<String, Long> functionalDomainEntities) {
        // 获取文件夹中的所有文件和子文件夹
        File[] files = folder.listFiles((dir, name) ->
                !name.startsWith("~$") &&
                        !name.startsWith("._"));
        if (files != null) {

            for (File file : files) {
                if (file.isFile()) {
                    // 获取功能域名称
                    String domainName = file.getName()
                            .substring(file.getName().lastIndexOf('-')+1,
                                    file.getName().length());

                    // 获取功能域id
                    Long domainId = functionalDomainEntities.get(domainName.substring(0,domainName.indexOf(".")));

                    // 如果是图片文件，输出图片名称
                    if (isImageFile(file)) {
                        log.info("图片文件: " + file.getName());
                        /**
                        if(domainName.contains("封面")){

                            // B1-极氪-007-KrGPT6.2-出行域
                            String domainNameF = file.getName()
                                    .substring(0,file.getName().lastIndexOf('-'));

                            String domainNameL = domainNameF.substring(domainNameF.lastIndexOf('-')+1,domainNameF.length());

                            Long domainId1 = functionalDomainEntities.get(domainNameL);

                            String videoName = file.getName().substring(0,file.getName().indexOf("-"));

                            boolean vehicleFunctionDomainVideo = isVehicleFunctionDomainVideo(vehicleId + "",
                                    domainId1 + ""
                                    , getGoodOrBad(file.getName().charAt(0)),
                                    (domainId1 + vehicleId) + videoName
                                    , 1);
                            long l = snowflakeIdGenerator.nextId();
                            if(vehicleFunctionDomainVideo){
                                FunctionDomainVideoEntity functionDomainVideoEntity = new FunctionDomainVideoEntity();
                                functionDomainVideoEntity.setId(l);
                                functionDomainVideoEntity.setVehicleId(vehicleId);
                                functionDomainVideoEntity.setFunctionDomainId(domainId1);
                                functionDomainVideoEntity.setType(getGoodOrBad(file.getName().charAt(0)));
                                functionDomainVideoEntity.setUrlName(l+videoName);
                                functionDomainVideoEntity.setFileType(1);
                                functionDomainVideoDao.insert(functionDomainVideoEntity);

                                //TODO 上传到阿里云OSS
                                OssUploadRequest ossUploadRequest = new OssUploadRequest();
                                ossUploadRequest.setFile_type("image");
                                ossUploadRequest.setFile_id(functionDomainVideoEntity.getUrlName());
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
                        }
                        */

                    }
                    /**
                    // 如果是视频文件，输出视频名称
                    else if (isVideoFile(file)) {
                        log.info("视频文件: " + file.getName());
                        String videoName = file.getName().substring(0, file.getName().indexOf("-"));
                        boolean vehicleFunctionDomainVideo = isVehicleFunctionDomainVideo(vehicleId + "",
                                domainId + ""
                                , getGoodOrBad(file.getName().charAt(0)),
                                videoName+(domainId + vehicleId)
                                , 0);
                        long l = snowflakeIdGenerator.nextId();
                        if(vehicleFunctionDomainVideo){
                            FunctionDomainVideoEntity functionDomainVideoEntity = new FunctionDomainVideoEntity();
                            functionDomainVideoEntity.setId(l);
                            functionDomainVideoEntity.setVehicleId(vehicleId);
                            functionDomainVideoEntity.setFunctionDomainId(domainId);
                            functionDomainVideoEntity.setType(getGoodOrBad(file.getName().charAt(0)));
                            functionDomainVideoEntity.setUrlName(videoName+l);
                            functionDomainVideoEntity.setFileType(0);
                            functionDomainVideoDao.insert(functionDomainVideoEntity);

                            //TODO 翻译字幕

                            //TODO 上传到阿里云OSS
                            OssUploadRequest ossUploadRequest = new OssUploadRequest();
                            ossUploadRequest.setFile_type("video");
                            ossUploadRequest
                                    .setFile_id(functionDomainVideoEntity.getUrlName());
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

                        //TODO 获取视频时长

                    }
                    */
                    //如果是字幕文件
                    else if(isStrFile(file) &&
                            file.getName().split("-")[file.getName().split("-").length-1]
                                    .equals("en.srt")){
                        log.info("字幕文件: " + file.getName());
                        Long functionDomainId = 0L;
                        for(String s:functionalDomainEntities.keySet()) {
                            if (file.getPath().contains(s)){
                                functionDomainId=functionalDomainEntities.get(s);
                            }
                        }

                        List<VideoNumberVo> VideoNumberList = functionDomainVideoDao.queryVideoNumberByVehicleIdAndFunctionDomainId(vehicleId,functionDomainId);

                        Map<String, String> collect = VideoNumberList.stream().collect(Collectors.toMap(VideoNumberVo::getNumber, VideoNumberVo::getId));

                        String id = collect.get(file.getName().split("-")[0]);

                        FunctionDomainVideoEntity byId = getById(id);

                        if (Objects.nonNull(byId)) {

                            byId.setIsSrt(1);

                            MultipartFile cMultiFile = fileConvertMultipartFile(file);
                            try {
                                String s = aLiYunOSSController.uploadVideoStr(cMultiFile, id + "-en.srt");
                                if ("上传成功".equals(s)) {

                                    updateById(byId);

                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } else if (file.isDirectory()) {
//                    log.info("进入文件夹: " + file.getName());
                    // 递归处理子文件夹
                    processFolderDomain(file,vehicleId,functionalDomainEntities);
                }
            }
        } else {
            log.info("文件夹为空: " + folder.getAbsolutePath());
        }
    }

    // 判断文件是否为字幕文件
    private boolean isStrFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".srt");
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

    private Integer getGoodOrBad(char name) {
        if (Character.toLowerCase(name)=='g') {
            return 1;
        }else {
            return 0;
        }
    }
    public String splitString(String str) {
        int lastIndex = str.lastIndexOf('\\');

        String result = str.substring(lastIndex+1, str.length());

        return result;
    }

    /**
     * 根据车辆id和功能域id获取测试用例并判断是否存在
     * true:存在 false:不存在
     * @param vehicleId
     * @param domainIndexId
     * @return
     */
    public boolean isVehicleDomainTestCase(String vehicleId,String domainIndexId){

        DomainTestCaseEntity domainTestCaseEntity = domainTestCaseService.getByVehicleIdAndDomainIndexId(vehicleId,domainIndexId);

        return Objects.nonNull(domainTestCaseEntity);
    }

    /**
     * 获取功能域视频信息是否存在
     * true:存在 false:不存在
     * @param vehicleId
     * @param domainId
     * @param type
     * @param videoName
     * @param fileType
     * @return
     */
    public boolean isVehicleFunctionDomainVideo(String vehicleId,String domainId,Integer type,String videoName,Integer fileType){

        FunctionDomainVideoEntity functionDomainVideoEntity = functionDomainVideoDao.getBy(vehicleId,domainId,type,videoName,fileType);

        return Objects.isNull(functionDomainVideoEntity);
    }

}
