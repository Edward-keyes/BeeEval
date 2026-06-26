package com.xailab.vehicle.operation.testplatform.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: TestCaseMaterialVo
 * @Description:
 * @author: liulin
 * @date: 2025/5/29 22:51
 */
@Data
public class TestCaseMaterialVo implements Serializable {

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
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
