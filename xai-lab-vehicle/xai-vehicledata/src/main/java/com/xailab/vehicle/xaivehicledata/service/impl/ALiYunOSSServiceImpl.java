package com.xailab.vehicle.xaivehicledata.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.xailab.vehicle.xaicommon.utils.JsonUtils;
import com.xailab.vehicle.xaicommon.utils.Okhttp3Utils;
import com.xailab.vehicle.xaicommon.utils.SnowflakeIdGenerator;
import com.xailab.vehicle.xaivehicledata.config.ALiYunOssConfig;
import com.xailab.vehicle.xaivehicledata.entity.request.OssUploadRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.OSSResponse;
import com.xailab.vehicle.xaivehicledata.service.ALiYunOSSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ALiYunOSSServiceImpl implements ALiYunOSSService {

    private final ALiYunOssConfig ossConfig;

    private static final SnowflakeIdGenerator snowflakeIdGenerator
            = new SnowflakeIdGenerator(0, 0);

    // 创建 OkHttpClient 实例
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(50000, TimeUnit.SECONDS) // 连接超时时间
            .writeTimeout(50000, TimeUnit.SECONDS)  // 写入超时时间
            .readTimeout(50000, TimeUnit.SECONDS)   // 读取超时时间
            .build();


    @Override
    public String queryPhoto(String photoName) {

        Okhttp3Utils okhttp3Utils = new Okhttp3Utils();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        try {
            String data = okhttp3Utils.getData(ossConfig.getGetImageUrl() + "?file_id=" + photoName, headers);
            OSSResponse ossResponse = JsonUtils.jsonToObj(data, OSSResponse.class);
            return ossResponse.getUrl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String queryVideo(String videoName) {
        Okhttp3Utils okhttp3Utils = new Okhttp3Utils();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        try {
            String data = okhttp3Utils.getData(ossConfig.getGetPlayList() + "?file_id=" + videoName, headers);
            OSSResponse ossResponse = JsonUtils.jsonToObj(data, OSSResponse.class);
            return ossResponse.getPlaylist_url();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String queryStr(String strName) {

        // 获取阿里云OSS参数
        String endpoint = ossConfig.getEndpoint();

        String bucketName = ossConfig.getBucketName();

        String accessKeyId = ossConfig.getAccessKeyId();

        String accessKeySecret = ossConfig.getAccessKeySecret();

        String subtitle = ossConfig.getSubtitle();

        // 设置预签名URL的过期时间（例如1小时后过期）
        Date expiration = new Date(new Date().getTime() + 360 * 1000L);

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 生成预签名URL
        URL url = ossClient.generatePresignedUrl(bucketName, subtitle+strName, expiration);
        String replace = url+"";
        String replace1 = replace.replace("http", "https");
        System.out.println(replace1);
        // 关闭ossClient
        ossClient.shutdown();
        return replace1;
    }

    @Override
    public String uploadPhoto(MultipartFile file1) {
        String fileId;
        try {
            File file = convertMultipartToFile(file1);

            fileId=snowflakeIdGenerator.nextId()+ "";
            log.info("图片文件: " + file.getName());
            //TODO 上传到阿里云OSS
            OssUploadRequest ossUploadRequest = new OssUploadRequest();
            ossUploadRequest.setFile_type("image");
            ossUploadRequest
                    .setFile_id(fileId);

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
                System.out.println(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileId;
    }

    public static File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
        // 创建临时文件（系统自动管理，应用退出时自动删除）
        File file = File.createTempFile("temp-", "-" + multipartFile.getOriginalFilename());

        // 将 MultipartFile 内容写入文件
        multipartFile.transferTo(file);

        return file;
    }
}
