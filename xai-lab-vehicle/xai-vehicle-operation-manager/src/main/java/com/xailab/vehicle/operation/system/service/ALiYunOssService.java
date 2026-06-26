package com.xailab.vehicle.operation.system.service;


import org.springframework.web.multipart.MultipartFile;

public interface ALiYunOssService{

    String download(MultipartFile file, String unzipPath, String uploadPath);

    String downloadAndUploadRawVideo(MultipartFile file,String sourceFolder,String targetFolder);

    String queryVideo(String name);
}
