package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class PcafeRelevancyDomainIndex {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer caseId;

    private Long beeevalCaseId;
}
