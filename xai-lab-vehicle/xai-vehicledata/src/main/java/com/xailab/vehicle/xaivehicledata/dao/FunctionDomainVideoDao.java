package com.xailab.vehicle.xaivehicledata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xailab.vehicle.xaivehicledata.entity.FunctionDomainVideoEntity;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionVideoListVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionalVideoNewVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionalVideoVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.VideoNumberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FunctionDomainVideoDao extends BaseMapper<FunctionDomainVideoEntity> {
    FunctionDomainVideoEntity getBy(@Param("vehicleId") String vehicleId, @Param("domainId") String domainId,
            @Param("type") Integer type, @Param("name") String name, @Param("fileType") Integer fileType);

    List<FunctionVideoListVo> selectListQ(@Param("vehicleId") String vehicleId, @Param("i") int i,
            @Param("language") String language);

    /**
     * 分页查询功能表现视频列表
     * 
     * @param vehicleId 车辆ID
     * @param i         类型：1=good, 0=bad
     * @param language  语言
     * @param offset    偏移量
     * @param limit     限制数量
     * @return 视频列表
     */
    List<FunctionVideoListVo> selectListQWithPagination(@Param("vehicleId") String vehicleId, @Param("i") int i,
            @Param("language") String language, @Param("offset") int offset, @Param("limit") int limit);

    List<FunctionalVideoNewVo> selectListNewQ(@Param("vehicleId") String vehicleId, @Param("i") int i,
            @Param("language") String language);

    /**
     * 分页查询新版功能表现视频列表
     * 
     * @param vehicleId 车辆ID
     * @param i         类型：1=good, 0=bad
     * @param language  语言
     * @param offset    偏移量
     * @param limit     限制数量
     * @return 视频列表
     */
    List<FunctionalVideoNewVo> selectListNewQWithPagination(@Param("vehicleId") String vehicleId, @Param("i") int i,
            @Param("language") String language, @Param("offset") int offset, @Param("limit") int limit);

    List<VideoNumberVo> queryVideoNumberByVehicleIdAndFunctionDomainId(@Param("vehicleId") Long vehicleId,
            @Param("functionDomainId") Long functionDomainId);
}
