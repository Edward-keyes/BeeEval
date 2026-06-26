package com.xailab.vehicle.operation.testplatform.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: FunctionTreeCaseVo
 * @Description:
 * @author: liulin
 * @date: 2025/5/3 3:01
 */
@Data
public class FunctionTreeCaseVo implements Serializable {
    /**
     * id
     */
    @TableId
    private Integer id;

    /**
     * 用例内容
     */
    private String testcaseContent;

    /**
     * 一级指标
     */
    private String primaryMetric;

    /**
     * 二级指标
     */
    private String secondaryMetric;

    /**
     * 三级指标
     */
    private String tertiaryMetric;

    /**
     * 功能域id(对应plan_detail表)
     */
    private Integer functionId;

    /**
     * 场景id
     */
    private Integer scenarioId;

    /**
     * 场景任务
     */
    private String scenarioTask;

    /**
     * 任务细分
     */
    private String taskDetail;

    /**
     * 计分方式
     */
    private String scoringMethod;

    /**
     * 评分标准
     */
    private String scoringCriteria;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    /**
     * 测试文件素材用例评级
     * @see  FunctionTreeCaseMaterialStateEnum
     */
    private String testCaseRate;

    /**
     * 测试用例测试结果素材状态
     * 未验证/Avg/Good/Poor
     * na/avg/good/poor
     *  @see  FunctionTreeCaseRateEnum
     */
    private String materialState;


}
