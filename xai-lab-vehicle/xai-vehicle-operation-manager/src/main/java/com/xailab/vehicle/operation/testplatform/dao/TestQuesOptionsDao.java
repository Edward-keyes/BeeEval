package com.xailab.vehicle.operation.testplatform.dao;

import com.xailab.vehicle.framework.mybatis.dao.BaseDao;
import com.xailab.vehicle.operation.testplatform.entity.TestQuesOptionsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
* 功能评价选项表
*
* @author 阿沐 babamu@126.com
* @since 1.0.0 2025-04-26
*/
@Mapper
public interface TestQuesOptionsDao extends BaseDao<TestQuesOptionsEntity> {

    @Select("""
        select Max(`sort`) from test_ques_options where ques_id = #{quesId}
    """)
    Integer findMaxSort(@Param("quesId") Integer quesId);
}