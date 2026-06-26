package com.xailab.vehicle.xaivehicledata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaivehicledata.dao.VehicleDomainScoreDao;
import com.xailab.vehicle.xaivehicledata.entity.VehicleDomainScoreEntity;
import com.xailab.vehicle.xaivehicledata.service.VehicleDomainScoreService;
import org.springframework.stereotype.Service;

@Service("vehicleDomainScoreService")
public class VehicleDomainScoreServiceImpl extends ServiceImpl<VehicleDomainScoreDao, VehicleDomainScoreEntity> implements VehicleDomainScoreService {
}
