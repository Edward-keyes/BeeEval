package com.xailab.vehicle.xaivehicledata.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.xailab.vehicle.xaivehicledata.config.ALiYunOssConfig;
import com.xailab.vehicle.xaivehicledata.service.ALiYunOSSService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/aliyunoss")
@RequiredArgsConstructor
public class ALiYunOSSController {

    private final ALiYunOssConfig ossConfig;

    @Resource
    private ALiYunOSSService aLiYunOSSService;

    @PostMapping("/uploadVideoStr")
    public String uploadVideoStr(MultipartFile file,String name) throws IOException {

        // 获取阿里云OSS参数
        String endpoint = ossConfig.getEndpoint();

        String bucketName = ossConfig.getBucketName();

        String accessKeyId = ossConfig.getAccessKeyId();

        String accessKeySecret = ossConfig.getAccessKeySecret();

        String subtitle = ossConfig.getSubtitle();

        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 避免文件覆盖，需要使用UUID将文件重命名
        String originalFilename = file.getOriginalFilename();

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, subtitle+name, inputStream);

        // 关闭ossClient
        ossClient.shutdown();

        // 把上传到oss的路径返回
        return "上传成功";
	}

    @PostMapping("/uploadPhoto")
    public String uploadPhoto(MultipartFile file){

        String s = aLiYunOSSService.uploadPhoto(file);

        return s;

    }

}
