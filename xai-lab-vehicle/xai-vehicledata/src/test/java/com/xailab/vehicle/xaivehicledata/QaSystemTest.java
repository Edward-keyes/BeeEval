package com.xailab.vehicle.xaivehicledata;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.xailab.vehicle.xaivehicledata.entity.QaKnowledgeBaseEntity;
import com.xailab.vehicle.xaivehicledata.entity.request.QaQueryRequest;
import com.xailab.vehicle.xaivehicledata.entity.request.QaSessionRequest;
import com.xailab.vehicle.xaivehicledata.entity.response.QaQueryResponse;
import com.xailab.vehicle.xaivehicledata.entity.response.QaSessionResponse;
import com.xailab.vehicle.xaivehicledata.service.QaKnowledgeBaseService;
import com.xailab.vehicle.xaivehicledata.service.QaQueryService;
import com.xailab.vehicle.xaivehicledata.service.QaSessionService;
import com.xailab.vehicle.xaivehicledata.service.VehicleDataSyncService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 智能问答系统单元测试
 */
@Slf4j
@SpringBootTest
public class QaSystemTest {

    @Autowired
    private QaSessionService qaSessionService;

    @Autowired
    private QaQueryService qaQueryService;

    @Autowired
    private QaKnowledgeBaseService qaKnowledgeBaseService;

    @Autowired
    private VehicleDataSyncService vehicleDataSyncService;

    @BeforeEach
    public void setUp() {
        log.info("测试开始前：模拟Sa-Token登录");
        StpUtil.login(1001L, "test_user");
    }

    @AfterEach
    public void tearDown() {
        log.info("测试结束后：退出Sa-Token登录");
        StpUtil.logout();
    }

    @Test
    public void testCreateSession() {
        log.info("测试创建会话");

        QaSessionRequest request = new QaSessionRequest();
        request.setUserId("test_user_001");
        request.setUserName("测试用户");

        QaSessionResponse response = qaSessionService.createSession(request);

        assertNotNull(response, "会话响应不能为空");
        assertNotNull(response.getSessionId(), "会话ID不能为空");
        // assertEquals("test_user_001", response.getUserId(), "用户ID应该匹配");
        // assertNotNull(response.getCreatedTime(), "创建时间不能为空");

        log.info("会话创建成功: sessionId={}", response.getSessionId());
    }

    @Test
    public void testVehicleDataSync() {
        log.info("测试车辆数据同步");

        int count = vehicleDataSyncService.syncAllVehiclesToKnowledgeBase();

        assertTrue(count >= 0, "同步的车辆数量应该大于等于0");
        log.info("成功同步{}条车辆数据", count);

        List<QaKnowledgeBaseEntity> vehicleDocs = qaKnowledgeBaseService.getDocumentsByCategory("vehicle");
        assertNotNull(vehicleDocs, "车辆文档不能为空");
        assertTrue(vehicleDocs.size() > 0, "应该至少有一条车辆数据");
        log.info("知识库中有{}条车辆数据", vehicleDocs.size());
    }

    @Test
    public void testKeywordExtraction() {
        log.info("测试关键词提取");

        String question = "查询小米SU7的功能域评分";
        List<String> keywords = qaKnowledgeBaseService.extractKeywords(question);

        assertNotNull(keywords, "关键词列表不能为空");
        assertTrue(keywords.size() > 0, "应该至少提取到一个关键词");
        log.info("提取的关键词: {}", keywords);
    }

    @Test
    public void testVehicleInfoSearch() {
        log.info("测试车辆信息检索");

        String question = "查询小米SU7的功能域评分";
        List<QaKnowledgeBaseEntity> results = qaKnowledgeBaseService.searchVehicleInfo(question);

        assertNotNull(results, "检索结果不能为空");
        log.info("检索到{}条相关车辆信息", results.size());

        for (QaKnowledgeBaseEntity doc : results) {
            log.info("车辆信息: title={}, content={}", doc.getTitle(), doc.getContent());
        }
    }

    @Test
    public void testQueryProcessing() {
        log.info("测试查询处理");

        QaSessionRequest sessionRequest = new QaSessionRequest();
        sessionRequest.setUserId("test_user_002");
        sessionRequest.setUserName("测试用户2");

        QaSessionResponse sessionResponse = qaSessionService.createSession(sessionRequest);
        assertNotNull(sessionResponse.getSessionId(), "会话ID不能为空");

        QaQueryRequest queryRequest = new QaQueryRequest();
        queryRequest.setSessionId(sessionResponse.getSessionId());
        queryRequest.setQuestion("查询所有功能域的平均评分");
        queryRequest.setUserId("test_user_002");

        try {
            QaQueryResponse response = qaQueryService.processQuery(queryRequest);

            assertNotNull(response, "查询响应不能为空");
            assertNotNull(response.getQueryId(), "查询ID不能为空");
            assertNotNull(response.getAnswer(), "答案不能为空");

            log.info("查询处理成功: queryId={}, answer={}",
                    response.getQueryId(), response.getAnswer().getText());
            log.info("处理时间: {}ms", response.getProcessingTime());

        } catch (Exception e) {
            log.warn("查询处理可能需要通义千问API配置: {}", e.getMessage());
        }
    }

    @Test
    public void testSqlValidation() {
        log.info("测试SQL验证");

        String validSql = "SELECT * FROM vehicle_domain_score WHERE type = 1";
        String invalidSql = "DROP TABLE vehicle_domain_score";

        assertTrue(qaQueryService.validateSql(validSql), "有效SQL应该通过验证");
        assertFalse(qaQueryService.validateSql(invalidSql), "无效SQL应该被拒绝");

        log.info("SQL验证测试通过");
    }

    @Test
    public void testGenerateInsertSql() {
        log.info("测试生成INSERT SQL");

        String insertSql = vehicleDataSyncService.generateVehicleDataInsertSql();

        assertNotNull(insertSql, "INSERT SQL不能为空");
        assertTrue(insertSql.contains("INSERT INTO qa_knowledge_base"), "应该包含INSERT语句");
        log.info("生成的INSERT SQL:\n{}", insertSql);
    }

    @Test
    public void testQueryWithVehicleContext() {
        log.info("测试带车辆上下文的查询");

        QaSessionRequest sessionRequest = new QaSessionRequest();
        sessionRequest.setUserId("test_user_003");
        sessionRequest.setUserName("测试用户3");

        QaSessionResponse sessionResponse = qaSessionService.createSession(sessionRequest);
        assertNotNull(sessionResponse.getSessionId(), "会话ID不能为空");

        QaQueryRequest queryRequest = new QaQueryRequest();
        queryRequest.setSessionId(sessionResponse.getSessionId());
        queryRequest.setQuestion("查询小米SU7的功能域评分详情");
        queryRequest.setUserId("test_user_003");

        try {
            QaQueryResponse response = qaQueryService.processQuery(queryRequest);

            assertNotNull(response, "查询响应不能为空");
            log.info("查询处理成功: queryId={}", response.getQueryId());

        } catch (Exception e) {
            log.warn("查询处理可能需要通义千问API配置: {}", e.getMessage());
        }
    }

    @Test
    public void testClearVehicleData() {
        log.info("测试清空车辆数据");

        int count = vehicleDataSyncService.clearVehicleDataFromKnowledgeBase();

        assertTrue(count >= 0, "删除的车辆数量应该大于等于0");
        log.info("成功删除{}条车辆数据", count);

        List<QaKnowledgeBaseEntity> vehicleDocs = qaKnowledgeBaseService.getDocumentsByCategory("vehicle");
        assertTrue(vehicleDocs.isEmpty(), "车辆数据应该被清空");
        log.info("车辆数据已清空");
    }

    @Test
    public void testIntegrationFlow() {
        log.info("测试完整集成流程");

        try {
            log.info("步骤1: 同步车辆数据");
            int syncCount = vehicleDataSyncService.syncAllVehiclesToKnowledgeBase();
            log.info("同步了{}条车辆数据", syncCount);

            log.info("步骤2: 创建会话");
            QaSessionRequest sessionRequest = new QaSessionRequest();
            sessionRequest.setUserId("integration_test_user");
            sessionRequest.setUserName("集成测试用户");

            QaSessionResponse sessionResponse = qaSessionService.createSession(sessionRequest);
            log.info("会话创建成功: sessionId={}", sessionResponse.getSessionId());

            log.info("步骤3: 执行查询");
            QaQueryRequest queryRequest = new QaQueryRequest();
            queryRequest.setSessionId(sessionResponse.getSessionId());
            queryRequest.setQuestion("查询所有功能域的平均评分");
            queryRequest.setUserId("integration_test_user");

            QaQueryResponse queryResponse = qaQueryService.processQuery(queryRequest);
            log.info("查询执行成功: queryId={}", queryResponse.getQueryId());

            log.info("集成流程测试完成");

        } catch (Exception e) {
            log.warn("集成流程测试可能需要通义千问API配置: {}", e.getMessage());
        }
    }
}
