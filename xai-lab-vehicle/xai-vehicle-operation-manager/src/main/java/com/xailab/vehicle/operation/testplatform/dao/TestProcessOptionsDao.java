package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.TestProcessOptionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* 流程选项表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface TestProcessOptionsDao extends BaseDao<TestProcessOptionsEntity> {
    @Select("""
        select Max(`sort`) from test_process_options where process_id = #{processId}
    """)
    Integer findMaxSort(@Param("processId") Integer processId);
}