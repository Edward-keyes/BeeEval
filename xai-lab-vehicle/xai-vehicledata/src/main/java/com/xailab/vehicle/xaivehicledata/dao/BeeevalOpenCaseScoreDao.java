package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.BeeevalOpenCaseScore;
import com.xailab.vehicle.xaivehicledata.entity.BeeevalOpenCaseScoreEntity;
import com.xailab.vehicle.xaivehicledata.entity.vo.CaseIdIndexVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 开源用例分数表DAO
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@Mapper
public interface BeeevalOpenCaseScoreDao extends BaseMapper<BeeevalOpenCaseScoreEntity> {
    CaseIdIndexVo queryOpenSourceCase();
}
