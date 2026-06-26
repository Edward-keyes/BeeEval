package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.AccountRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.LoginResponse;

public interface VehicleUserService extends IService<VehicleUserEntity> {
    Result signUpAccount(AccountRequest adminAccount);

    Result<LoginResponse> login(AccountRequest adminAccount);
}
