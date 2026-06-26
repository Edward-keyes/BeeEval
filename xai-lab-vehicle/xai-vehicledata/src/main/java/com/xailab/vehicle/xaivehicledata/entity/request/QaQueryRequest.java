package com.xailab.vehicle.xaivehicledata.entity.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 问答查询请求DTO
 */
@Data
public class QaQueryRequest {

    /**
     * 会话ID
     */
    @NotBlank(message = "会话ID不能为空")
    private String sessionId;

    /**
     * 用户问题
     */
    @NotBlank(message = "问题内容不能为空")
    private String question;

    /**
     * 使用的模板ID（可选）
     */
    private Long templateId;

    /**
     * 参数映射（可选）
     */
    private Map<String, Object> parameters;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;
}
