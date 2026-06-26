package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("beeeval_open_case_score")
public class BeeevalOpenCaseScore {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Long vehicleId;
    private Long threeTagId;
    private Integer caseId;
    private Integer score;

}
