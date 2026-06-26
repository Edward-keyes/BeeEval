package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xailab.vehicle.feign.pojo.response.FunctionTreeListResponse;
import com.xailab.vehicle.feign.pojo.treem.FunctionTreeQueryListResponse;
import com.xailab.vehicle.xaivehicledata.entity.FunctionThreeTagEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.response.FunctionTreeOpResponse;
import com.xailab.vehicle.xaivehicledata.entity.response.SynchronizationThreeTagResponse;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionTreeVideoNewVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 
 * 
 * 
 * @email d2460687074@gmail.com
 * @date 2025-01-15 10:30:59
 */
@Mapper
public interface FunctionThreeTagDao extends BaseMapper<FunctionThreeTagEntity> {

    List<FunctionTreeOpResponse> getFunctionTagList();

    List<FunctionThreeTagEntity> querySortThreeTag(@Param("tagNumber") String onlyTag);

    List<FunctionTreeListResponse> findFunctionTreeList();

    List<SynchronizationThreeTagResponse> getThreeTagListSynchronization();

    List<FunctionTreeVideoNewVo> queryCaseVideo(@Param("threeTagId")String threeTagId,@Param("vehicleId") String vehicleId);


    IPage<FunctionTreeQueryListResponse> queryFunctionTreePage(IPage<FunctionThreeTagEntity> page,
                                                               @Param("tagNumber") String tagNumber,
                                                               @Param("tagName") String tagName,
                                                               @Param("vehicleId") Long vehicleId);

    @Select("select * from vehicle_function_three_tag where tag_number=#{tagNumber}")
    FunctionThreeTagEntity queryOneByTagNumber(@Param("tagNumber") String tagNumber);
}
