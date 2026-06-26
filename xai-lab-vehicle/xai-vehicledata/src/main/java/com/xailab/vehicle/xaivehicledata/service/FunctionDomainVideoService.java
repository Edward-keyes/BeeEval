package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaivehicledata.entity.FunctionDomainVideoEntity;

public interface FunctionDomainVideoService extends IService<FunctionDomainVideoEntity> {


    void uploadVideoAndPicture(String path);

}
