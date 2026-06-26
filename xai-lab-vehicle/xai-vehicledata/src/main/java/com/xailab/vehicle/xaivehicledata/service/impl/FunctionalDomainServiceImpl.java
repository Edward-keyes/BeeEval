package com.xailab.vehicle.xaivehicledata.service.impl;

import com.xailab.vehicle.feign.vo.DomainRelevancyVo;
import com.xailab.vehicle.xaicommon.utils.Query;
import com.xailab.vehicle.xaivehicledata.entity.FunctionThreeTagEntity;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionDomainIndexMapVo;
import com.xailab.vehicle.xaivehicledata.entity.vo.FunctionDomainIndexVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xailab.vehicle.xaicommon.utils.PageUtils;
import com.xailab.vehicle.xaicommon.utils.R;


import com.xailab.vehicle.xaivehicledata.dao.FunctionalDomainDao;
import com.xailab.vehicle.xaivehicledata.entity.FunctionalDomainEntity;
import com.xailab.vehicle.xaivehicledata.service.FunctionalDomainService;


@Service("functionalDomainService")
public class FunctionalDomainServiceImpl extends ServiceImpl<FunctionalDomainDao, FunctionalDomainEntity> implements FunctionalDomainService {

    @Resource
    private FunctionalDomainDao functionalDomainDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FunctionalDomainEntity> page = this.page(
                new Query<FunctionalDomainEntity>().getPage(params),
                new QueryWrapper<FunctionalDomainEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Map<String, List<FunctionDomainIndexMapVo>> queryAllFunctionDomainData() {

        List<FunctionDomainIndexVo> list = functionalDomainDao.queryAllFunctionDomainData();


        return list.stream()
                // 按 functionalDomainName 分组
                .collect(Collectors.groupingBy(
                        FunctionDomainIndexVo::getFunctionalDomainName,
                        // 将每个 FunctionDomainIndexVo 转换为 FunctionDomainIndexMapVo
                        Collectors.mapping(vo -> {
                            FunctionDomainIndexMapVo mapVo = new FunctionDomainIndexMapVo();
                            mapVo.setFunctionalId(vo.getFunctionalId());
                            mapVo.setDomainIndexId(vo.getDomainIndexId());
                            mapVo.setIndexName(vo.getIndexName());
                            return mapVo;
                        }, Collectors.toList())
                ));
    }

    @Override
    public List<DomainRelevancyVo> queryRelevancy() {

        return functionalDomainDao.queryRelevancy();
    }

    @Override
    public List<com.xailab.vehicle.feign.vo.FunctionDomainIndexVo> queryRelevancyIndex() {
        return functionalDomainDao.queryRelevancyIndex();
    }

}