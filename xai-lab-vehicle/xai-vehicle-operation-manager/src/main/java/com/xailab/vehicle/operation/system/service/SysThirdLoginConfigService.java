package com.xailab.vehicle.operation.system.service;

import me.zhyd.oauth.request.AuthRequest;
import com.xailab.vehicle.framework.common.query.Query;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.system.entity.SysThirdLoginConfigEntity;
import com.xailab.vehicle.operation.system.vo.SysThirdLoginConfigVO;

import java.util.List;

/**
 * 第三方登录配置
 *

 */
public interface SysThirdLoginConfigService extends BaseService<SysThirdLoginConfigEntity> {

    PageResult<SysThirdLoginConfigVO> page(Query query);

    void save(SysThirdLoginConfigVO vo);

    void update(SysThirdLoginConfigVO vo);

    void delete(List<Long> idList);

    /**
     * 根据类型，获取授权请求
     *
     * @param openType 第三方登录类型
     */
    AuthRequest getAuthRequest(String openType);
}