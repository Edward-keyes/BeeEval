package com.xailab.vehicle.xaivehicledata.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("function_tree_case")
public class FunctionTreeCaseEntity {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 三级标签Number or 功能id
     */
    private String threeTagId;

    /**
     * 用例内容
     */
    private String caseContent;

    /**
     * 用例内容 en
     */
    private String caseContentEn;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
