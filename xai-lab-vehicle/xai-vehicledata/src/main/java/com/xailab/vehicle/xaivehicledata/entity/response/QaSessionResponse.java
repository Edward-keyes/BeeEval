package com.xailab.vehicle.xaivehicledata.entity.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 会话响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QaSessionResponse {

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 会话状态
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后活动时间
     */
    private LocalDateTime lastActivityTime;
}
