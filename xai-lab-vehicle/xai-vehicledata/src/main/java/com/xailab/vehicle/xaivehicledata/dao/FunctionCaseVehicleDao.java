package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.FunctionCaseMaterialEntity;
import com.xailab.vehicle.xaivehicledata.entity.FunctionCaseVehicleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 功能用例车辆关系表
 * 
 * @author caomei
 * @email d2460687074@gmail.com
 * @date 2025-06-08 01:22:23
 */
@Mapper
public interface FunctionCaseVehicleDao extends BaseMapper<FunctionCaseVehicleEntity> {

}
