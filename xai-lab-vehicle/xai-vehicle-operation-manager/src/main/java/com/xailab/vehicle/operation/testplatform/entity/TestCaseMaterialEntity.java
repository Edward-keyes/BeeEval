package com.xailab.vehicle.operation.testplatform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 测试用例素材管理
 * @ClassName: TestCaseMaterialEntity
 * @Description:
 * @author: liulin
 * @date: 2025/5/28 23:49
 */
@Data
@TableName("function_tree_test_case_material")
public class TestCaseMaterialEntity {

    @TableId
    private Long id;


    /**
     * 测试状态id
     */
    private Integer testStateId;

    /**
     * 文件对象名称
     */
    private String objectName;

    /**
     * 文件类型
     * 0/1 0图片 1视频
     */
    private Integer fileType;

    /**
     * 原始文件名称
     */
    private String originalFilename;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 是否显示 0/1 否/是
     */
    private Boolean isShow;


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
