package com.xailab.vehicle.operation.system.service;

import com.xailab.vehicle.operation.email.config.EmailConfig;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.system.entity.SysMailConfigEntity;
import com.xailab.vehicle.operation.system.query.SysMailConfigQuery;
import com.xailab.vehicle.operation.system.vo.SysMailConfigVO;

import java.util.List;

/**
 * 邮件平台
 *
 * 
 */
public interface SysMailConfigService extends BaseService<SysMailConfigEntity> {

    PageResult<SysMailConfigVO> page(SysMailConfigQuery query);

    List<SysMailConfigVO> list(Integer platform);

    /**
     * 启用的邮件平台列表
     */
    List<EmailConfig> listByEnable();

    void save(SysMailConfigVO vo);

    void update(SysMailConfigVO vo);

    void delete(List<Long> idList);
}