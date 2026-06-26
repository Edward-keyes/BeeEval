package com.xailab.vehicle.xaivehicledata.service;

import org.springframework.web.multipart.MultipartFile;

public interface ALiYunOSSService {

    public String queryPhoto(String photoName);

    public String queryVideo(String videoName);

    public String queryStr(String strName);

    public String uploadPhoto(MultipartFile file);
}
