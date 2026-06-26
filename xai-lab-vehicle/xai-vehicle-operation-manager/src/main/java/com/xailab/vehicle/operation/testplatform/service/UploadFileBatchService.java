package com.xailab.vehicle.operation.testplatform.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public interface UploadFileBatchService {

    public String uploadFileBatch(MultipartFile[] files, Integer recordId, String batchName, Integer materialClassify, Date executionTime);

}
