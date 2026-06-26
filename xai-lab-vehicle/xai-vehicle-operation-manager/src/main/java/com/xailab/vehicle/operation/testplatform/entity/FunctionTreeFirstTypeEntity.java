package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: FunctionTreeFirstTypeEntity
 * @Description:
 * @author: liulin
 * @date: 2025/5/31 0:31
 */
@Data
@TableName("function_tree_first_type")
public class FunctionTreeFirstTypeEntity {
    @TableId
    private Long id;

    /**
     * 标签
     */
    private String label;


    /**
     * 标签名称
     */
    private String labelName;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除 0/1 否/是
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

}
