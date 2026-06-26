package com.xailab.vehicle.operation.testplatform.pojo.response;

import lombok.Data;

@Data
public class StateDataResponse {

    /**
     * c.vehicle_name,
     * a.record_id,
     * d.id case_id,
     * d.testcase_content,
     * a.is_successful,
     * a.score,
     * a.error_type,
     * a.error_detail,a.other,
     * a.test_status
     */

    //车辆名称
    private String vehicleName;
    //记录id
    private Integer recordId;
    //用例id
    private Integer caseId;
    //用例内容
    private String testcaseContent;
    /**
     * -1:初始化 0:失败 1:成功
     */
    private Integer isSuccessful;
    /**
     * 分数
     */
    private Integer score;
    /**
     * 1.拒识
     * 2.兜底/不会
     * 3.幻觉
     * 4.ASR错误
     * 5.倾听中断
     * 6.输入不完整
     * 7.其他
     */
    private Integer errorType;
    /**
     * 错误详情 (为主)
     */
    private String errorDetail;
    /**
     * 其他错误详情
     */
    private String other;
    /**
     * 测试状态 未测试：0；已测试：1
     */
    private Integer testStatus;
    /**
     * -1 bad 0 review 1 good
     */
    private Integer caseType;
    /**
     * 功能域
     */
    private String functionName;
    /**
     * 任务细分
     */
    private String taskDetail;
    /**
     * 三级指标
     */
    private String tertiaryMetric;

}
