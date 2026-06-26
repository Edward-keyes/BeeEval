package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.BeeevalOpenSourceCaseEntity;
import com.xailab.vehicle.xaivehicledata.entity.vo.CaseIdIndexVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 200道开源用例表DAO
 *
 * @author caomei
 * @since 1.0.0 2025-01-11
 */
@Mapper
public interface BeeevalOpenSourceCaseDao extends BaseMapper<BeeevalOpenSourceCaseEntity> {

    /**
     * 查询开源用例的ID和domainIndexId映射关系
     * @return 开源用例映射列表
     */

    List<CaseIdIndexVo> queryOpenSourceCase();

}
