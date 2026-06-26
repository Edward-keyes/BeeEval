package com.xailab.vehicle.xaivehicledata.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xailab.vehicle.xaivehicledata.entity.DomainTestCaseEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.CaseRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.DomainTestCaseResponse;

import java.util.List;

public interface DomainTestCaseService extends IService<DomainTestCaseEntity> {
    DomainTestCaseEntity getByVehicleIdAndDomainIndexId(String vehicleId, String domainIndexId);

    void domainTestCaseDataInput(String path);

    List<DomainTestCaseResponse> queryCaseByVehicleIdAndDomainIndexId(CaseRequest caseRequest);
}
