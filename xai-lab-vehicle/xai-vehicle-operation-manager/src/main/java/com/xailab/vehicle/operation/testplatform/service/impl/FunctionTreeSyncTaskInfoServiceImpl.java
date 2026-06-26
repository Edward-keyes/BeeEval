package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import com.xailab.vehicle.framework.common.utils.PageResult;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.convert.FunctionTreeSyncTaskInfoConvert;
import com.xailab.vehicle.operation.testplatform.entity.FunctionTreeSyncTaskInfoEntity;
import com.xailab.vehicle.operation.testplatform.query.FunctionTreeSyncTaskInfoQuery;
import com.xailab.vehicle.operation.testplatform.vo.FunctionTreeSyncTaskInfoVO;
import com.xailab.vehicle.operation.testplatform.dao.FunctionTreeSyncTaskInfoDao;
import com.xailab.vehicle.operation.testplatform.service.FunctionTreeSyncTaskInfoService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能树同步任务详情表
 *
 * @author mm 
 * @since 1.0.0 2025-06-02
 */
@Service
@AllArgsConstructor
public class FunctionTreeSyncTaskInfoServiceImpl extends BaseServiceImpl<FunctionTreeSyncTaskInfoDao, FunctionTreeSyncTaskInfoEntity> implements FunctionTreeSyncTaskInfoService {

    @Override
    public PageResult<FunctionTreeSyncTaskInfoVO> page(FunctionTreeSyncTaskInfoQuery query) {
        IPage<FunctionTreeSyncTaskInfoEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(FunctionTreeSyncTaskInfoConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<FunctionTreeSyncTaskInfoEntity> getWrapper(FunctionTreeSyncTaskInfoQuery query){
        LambdaQueryWrapper<FunctionTreeSyncTaskInfoEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(FunctionTreeSyncTaskInfoVO vo) {
        FunctionTreeSyncTaskInfoEntity entity = FunctionTreeSyncTaskInfoConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(FunctionTreeSyncTaskInfoVO vo) {
        FunctionTreeSyncTaskInfoEntity entity = FunctionTreeSyncTaskInfoConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}