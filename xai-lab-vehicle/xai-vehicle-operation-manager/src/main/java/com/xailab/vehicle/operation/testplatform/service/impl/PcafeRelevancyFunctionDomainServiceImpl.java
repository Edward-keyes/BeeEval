package com.xailab.vehicle.operation.testplatform.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xailab.vehicle.feign.vehicledata.FunctionalDomainFeign;
import com.xailab.vehicle.feign.vo.FunctionDomainIndexVo;
import com.xailab.vehicle.framework.mybatis.service.impl.BaseServiceImpl;
import com.xailab.vehicle.operation.testplatform.dao.PcafeRelevancyFunctionDomainDao;
import com.xailab.vehicle.operation.testplatform.dao.TestPlatformVehicleTestCaseDao;
import com.xailab.vehicle.operation.testplatform.entity.PcafeRelevancyFunctionDomainEntity;
import com.xailab.vehicle.operation.testplatform.service.PcafeRelevancyFunctionDomainService;
import com.xailab.vehicle.operation.testplatform.vo.DomainCaseVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@DS("test_platform")
public class PcafeRelevancyFunctionDomainServiceImpl extends BaseServiceImpl<PcafeRelevancyFunctionDomainDao, PcafeRelevancyFunctionDomainEntity> implements PcafeRelevancyFunctionDomainService {

    @Resource
    TestPlatformVehicleTestCaseDao testPlatformVehicleTestCaseDao;

    @Resource
    FunctionalDomainFeign functionalDomainFeign;

    @Override
    public Boolean syncFunctionDomain() {
        List<FunctionDomainIndexVo> vo = functionalDomainFeign.queryRelevancyIndex();

        List<DomainCaseVo> list=testPlatformVehicleTestCaseDao.queryDomainCase();

        List<PcafeRelevancyFunctionDomainEntity> functionDomainEntityList = new ArrayList<>();

        Map<String, Long> collect = vo.stream()
                .collect(Collectors
                        .toMap(v -> v.getFunctionDomainName() + "-" + v.getDomainIndexName()
                                , FunctionDomainIndexVo::getIndexId));

        for (DomainCaseVo domainCaseVo : list) {

            Long l = collect.get(domainCaseVo.getFunctionDomain() + "-" + domainCaseVo.getDomainIndex());

            if (Objects.nonNull(l)) {

                PcafeRelevancyFunctionDomainEntity entity = new PcafeRelevancyFunctionDomainEntity();

                entity.setTestFunctionDomainName(domainCaseVo.getFunctionDomain());

                entity.setTestIndexName(domainCaseVo.getDomainIndex());

                entity.setBeeevalIndexId(l);

                functionDomainEntityList.add(entity);
            }
        }

        return saveBatch(functionDomainEntityList);
    }
}