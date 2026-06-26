package com.xailab.vehicle.operation.system.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.system.entity.SysDictTypeEntity;
import com.xailab.vehicle.operation.system.query.SysDictTypeQuery;
import com.xailab.vehicle.operation.system.vo.SysDictTypeVO;
import com.xailab.vehicle.operation.system.vo.SysDictVO;

import java.util.List;

/**
 * 数据字典
 *

 */
public interface SysDictTypeService extends BaseService<SysDictTypeEntity> {

    PageResult<SysDictTypeVO> page(SysDictTypeQuery query);

    List<SysDictTypeVO> list(Long pid);

    void save(SysDictTypeVO vo);

    void update(SysDictTypeVO vo);

    void delete(List<Long> idList);

    /**
     * 获取动态SQL数据
     */
    List<SysDictVO.DictData> getDictSql(Long id);

    /**
     * 获取全部字典列表
     */
    List<SysDictVO> getDictList();

    /**
     * 刷新字典缓存
     */
    void refreshTransCache();

}