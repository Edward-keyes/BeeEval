package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("vehicle_domain_score")
public class VehicleDomainScoreEntity {
    @TableId(type = IdType.AUTO )
    private Integer id;
    private Long vehicleId;
    private Long domainId;
    private Short type;
    private Double score;
}
