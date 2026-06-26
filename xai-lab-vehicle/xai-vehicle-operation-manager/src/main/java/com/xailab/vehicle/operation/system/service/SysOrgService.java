package com.xailab.vehicle.operation.system.service;

import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.system.entity.SysOrgEntity;
import com.xailab.vehicle.operation.system.vo.SysOrgVO;

import java.util.List;

/**
 * 机构管理
 *

 */
public interface SysOrgService extends BaseService<SysOrgEntity> {

    List<SysOrgVO> getList();

    void save(SysOrgVO vo);

    void update(SysOrgVO vo);

    void delete(Long id);

    /**
     * 根据机构ID，获取子机构ID列表(包含本机构ID)
     *
     * @param id 机构ID
     */
    List<Long> getSubOrgIdList(Long id);

    /**
     * 根据机构ID列表，获取机构名称列表
     *
     * @param idList 机构ID列表
     */
    List<String> getNameList(List<Long> idList);
}