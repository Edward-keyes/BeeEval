package com.xailab.vehicle.operation.beeeval.dao;

import com.xailab.vehicle.operation.beeeval.entity.vo.TestCaseThreeTagVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PcafeThreeTagDao {

    List<TestCaseThreeTagVo> getPcafeThreeTagList();

}
