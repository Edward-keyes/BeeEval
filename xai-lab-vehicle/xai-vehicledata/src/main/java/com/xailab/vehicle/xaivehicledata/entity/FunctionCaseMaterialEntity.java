package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("function_case_material")
public class FunctionCaseMaterialEntity {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * FunctionTreeCaseId
     */
    private Long functionTreeCaseId;

    /**
     * 素材url
     */
    private String materialUrl;

    /**
     * 素材类型
     */
    private String materialType;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}
