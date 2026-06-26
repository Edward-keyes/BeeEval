package com.xailab.vehicle.xaivehicledata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.feign.vo.OpenSourceVo;
import com.xailab.vehicle.xaivehicledata.entity.BeeevalOpenCaseScoreEntity;

import java.util.List;

public interface BeeevalOpenCaseScoreService extends IService<BeeevalOpenCaseScoreEntity> {

    Boolean saveCaseScoreByVehicleId(String vehicleId, List<OpenSourceVo> openSourceVos);

}
