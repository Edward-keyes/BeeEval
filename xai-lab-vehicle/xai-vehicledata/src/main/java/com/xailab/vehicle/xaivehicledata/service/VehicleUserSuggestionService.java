package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaicommon.utils.Result;
import com.xailab.vehicle.xaivehicledata.entity.VehicleUserSuggestionEntity;

public interface VehicleUserSuggestionService extends IService<VehicleUserSuggestionEntity> {
    /**
     * 保存用户反馈
     */
    Result saveSuggestion(VehicleUserSuggestionEntity suggestion);

    /**
     * 获取用户反馈列表
     */
    Result getSuggestionList();
}