package com.xailab.vehicle.xaivehicledata.controller;

import com.xailab.vehicle.xaivehicledata.common.ApiResponse;
import com.xailab.vehicle.xaivehicledata.entity.request.QaQueryRequest;
import com.xailab.vehicle.xaivehicledata.entity.request.QaSessionRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.QaQueryResponse;
import com.xailab.vehicle.xaivehicledata.entity.response.QaSessionResponse;
import com.xailab.vehicle.xaivehicledata.entity.vo.QaSessionHistoryVO;
import com.xailab.vehicle.xaivehicledata.service.QaQueryService;
import com.xailab.vehicle.xaivehicledata.service.QaSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 智能问答控制器
 */
@Slf4j
@Tag(name = "智能问答接口", description = "智能问答相关API")
@RestController
@RequestMapping("/api/v1/qa")
@RequiredArgsConstructor
public class QaController {

    private final QaQueryService qaQueryService;
    private final QaSessionService qaSessionService;

    @Operation(summary = "创建问答会话", description = "创建新的智能问答会话")
    @SaCheckLogin
    @PostMapping("/session")
    public ApiResponse<QaSessionResponse> createSession(
            @Valid @RequestBody QaSessionRequest request) {

        log.info("Creating QA session for user: {}", request.getUserId());
        QaSessionResponse response = qaSessionService.createSession(request);

        return ApiResponse.success(response);
    }

    @Operation(summary = "提交问题查询", description = "提交用户问题进行智能问答")
    @SaCheckLogin
    @PostMapping("/query")
    public ApiResponse<QaQueryResponse> processQuery(
            @Valid @RequestBody QaQueryRequest request,
            HttpServletRequest httpRequest) {

        log.info("Processing query for session: {}, question: {}",
                request.getSessionId(), request.getQuestion());

        // 设置用户IP（实际项目中可能需要从网关获取）
        String clientIp = getClientIp(httpRequest);
        log.debug("Request from IP: {}", clientIp);

        QaQueryResponse response = qaQueryService.processQuery(request);

        return ApiResponse.success(response);
    }

    @Operation(summary = "重新生成答案", description = "重新生成指定查询的答案")
    @SaCheckLogin
    @PostMapping("/query/{queryId}/regenerate")
    public ApiResponse<QaQueryResponse> regenerateAnswer(
            @Parameter(description = "查询ID") @PathVariable String queryId) {

        log.info("Regenerating answer for query: {}", queryId);
        QaQueryResponse response = qaQueryService.regenerateAnswer(queryId);

        return ApiResponse.success(response);
    }

    @Operation(summary = "获取查询详情", description = "获取指定查询的详细信息")
    @SaCheckLogin
    @GetMapping("/query/{queryId}")
    public ApiResponse<QaQueryResponse> getQueryDetail(
            @Parameter(description = "查询ID") @PathVariable String queryId) {

        log.info("Getting query detail: {}", queryId);
        QaQueryResponse response = qaQueryService.getQueryDetail(queryId);

        return response != null ? ApiResponse.success(response) : ApiResponse.error("查询记录不存在");
    }

    @Operation(summary = "获取会话历史", description = "获取指定会话的查询历史")
    @SaCheckLogin
    @GetMapping("/session/{sessionId}/history")
    public ApiResponse<List<QaSessionHistoryVO>> getSessionHistory(
            @Parameter(description = "会话ID") @PathVariable String sessionId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer size) {

        log.info("Getting session history: {}, page: {}, size: {}", sessionId, page, size);
        List<QaSessionHistoryVO> history = qaSessionService.getSessionHistory(sessionId, page, size);

        return ApiResponse.success(history);
    }

    @Operation(summary = "删除会话", description = "删除指定的问答会话")
    @SaCheckLogin
    @DeleteMapping("/session/{sessionId}")
    public ApiResponse<Void> deleteSession(
            @Parameter(description = "会话ID") @PathVariable String sessionId) {

        log.info("Deleting session: {}", sessionId);
        qaSessionService.deleteSession(sessionId);

        return ApiResponse.success(null);
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多IP情况，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
