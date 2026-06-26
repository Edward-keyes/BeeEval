package com.xailab.vehicle.operation.system.service;

import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.BaseService;
import com.xailab.vehicle.operation.system.entity.SysAttachmentEntity;
import com.xailab.vehicle.operation.system.query.SysAttachmentQuery;
import com.xailab.vehicle.operation.system.vo.SysAttachmentVO;

import java.util.List;

/**
 * 附件管理
 *

 */
public interface SysAttachmentService extends BaseService<SysAttachmentEntity> {

    PageResult<SysAttachmentVO> page(SysAttachmentQuery query);

    void save(SysAttachmentVO vo);

    void update(SysAttachmentVO vo);

    void delete(List<Long> idList);
}