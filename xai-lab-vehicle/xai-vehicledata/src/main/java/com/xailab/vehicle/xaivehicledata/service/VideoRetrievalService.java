package com.xailab.vehicle.xaivehicledata.service;

public interface VideoRetrievalService {

    String downloadEncryptedVideo(Long videoId);

    String decryptVideo(String encryptedFilePath);

    String getSignedUrl(Long videoId, int expireSeconds);

    void cleanupTempFile(String filePath);
}
