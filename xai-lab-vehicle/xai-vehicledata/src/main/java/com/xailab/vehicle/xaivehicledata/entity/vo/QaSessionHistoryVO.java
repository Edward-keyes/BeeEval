package com.xailab.vehicle.xaivehicledata.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会话历史VO
 */
@Data
public class QaSessionHistoryVO {

    /**
     * 查询ID
     */
    private Long queryId;

    /**
     * 用户问题
     */
    private String question;

    /**
     * 生成的答案
     */
    private String answer;

    /**
     * 查询状态
     */
    private String status;

    /**
     * 响应时间(毫秒)
     */
    private Integer responseTime;

    /**
     * 是否缓存命中
     */
    private Boolean isCacheHit;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 可视化类型
     */
    private String visualizationType;
}
