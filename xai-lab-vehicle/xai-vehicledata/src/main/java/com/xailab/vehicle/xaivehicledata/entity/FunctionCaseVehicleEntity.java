package com.xailab.vehicle.xaivehicledata.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("function_case_vehicle")
public class FunctionCaseVehicleEntity {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用例id
     */
    private Long caseId;

    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * 是否首页展示
     * 0：否/1：是
     */
    private Integer isShow;

    /**
     * 功能评价
     * 0：na/1：modest/2：avg/3：good
     */
    private String functionEvaluation;

    /**
     * 用例选项 数据
     */
    private String caseOptions;


}
