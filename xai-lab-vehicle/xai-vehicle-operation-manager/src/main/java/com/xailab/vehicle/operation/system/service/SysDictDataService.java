package com.xailab.vehicle.operation.system.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.system.entity.SysDictDataEntity;
import com.xailab.vehicle.operation.system.query.SysDictDataQuery;
import com.xailab.vehicle.operation.system.vo.SysDictDataVO;

import java.util.List;

/**
 * 数据字典
 *

 */
public interface SysDictDataService extends BaseService<SysDictDataEntity> {

    PageResult<SysDictDataVO> page(SysDictDataQuery query);

    void save(SysDictDataVO vo);

    void update(SysDictDataVO vo);

    void delete(List<Long> idList);

}