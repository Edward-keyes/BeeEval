package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.VehicleTestResultAllEntity;
import com.xailab.vehicle.xaivehicledata.entity.response.TestResultResponse;
import com.xailab.vehicle.xaivehicledata.entity.vo.Problem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VehicleTestResultAllDao extends BaseMapper<VehicleTestResultAllEntity> {
    List<Problem> queryVehicleScoreByVIdAndTId(@Param("l") long l, @Param("functionIndexId") String functionIndexId);
}