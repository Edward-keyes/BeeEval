package com.xailab.vehicle.operation.system.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.sms.config.SmsConfig;
import com.xailab.vehicle.operation.system.entity.SysSmsConfigEntity;
import com.xailab.vehicle.operation.system.query.SysSmsConfigQuery;
import com.xailab.vehicle.operation.system.vo.SysSmsConfigVO;

import java.util.List;

/**
 * 短信配置
 *

 */
public interface SysSmsConfigService extends BaseService<SysSmsConfigEntity> {

    PageResult<SysSmsConfigVO> page(SysSmsConfigQuery query);

    List<SysSmsConfigVO> list(Integer platform);

    /**
     * 启用的短信平台列表
     */
    List<SmsConfig> listByEnable();

    void save(SysSmsConfigVO vo);

    void update(SysSmsConfigVO vo);

    void delete(List<Long> idList);

}