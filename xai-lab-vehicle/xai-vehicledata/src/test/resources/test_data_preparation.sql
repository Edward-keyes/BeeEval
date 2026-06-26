-- 测试数据准备脚本
-- 用于验证视频分析RAG系统

-- 1. 确保已有测试车辆数据
SELECT '检查车辆数据...' AS step;
SELECT COUNT(*) AS vehicle_count FROM vehicle WHERE id IN (1, 2, 3);

-- 2. 检查功能域数据
SELECT '检查功能域数据...' AS step;
SELECT COUNT(*) AS domain_count FROM vehicle_functional_domain;

-- 3. 检查三级指标数据
SELECT '检查三级指标数据...' AS step;
SELECT COUNT(*) AS index_count FROM vehicle_domain_index;

-- 4. 检查视频数据
SELECT '检查视频数据...' AS step;
SELECT 
    id,
    vehicle_id,
    function_domain_id,
    function_domain_index_id,
    type,
    task_type,
    description,
    video_url
FROM vehicle_function_domain_video 
LIMIT 5;

-- 5. 检查得分数据
SELECT '检查得分数据...' AS step;
SELECT 
    type,
    COUNT(*) AS count
FROM vehicle_domain_score
GROUP BY type;

-- 6. 检查知识库数据
SELECT '检查知识库数据...' AS step;
SELECT 
    category,
    COUNT(*) AS count
FROM qa_knowledge_base
GROUP BY category;

-- 7. 检查视频分析结果表是否存在
SELECT '检查视频分析结果表...' AS step;
SELECT COUNT(*) AS analysis_count FROM video_analysis_result;

-- 8. 查看可用的测试视频
SELECT '可用的测试视频...' AS step;
SELECT 
    v.id AS video_id,
    v.vehicle_id,
    v.type AS video_type,
    v.task_type,
    v.description,
    CASE WHEN v.type = 1 THEN '优秀案例' ELSE '问题案例' END AS case_type,
    vd.name AS domain_name,
    vi.name AS index_name
FROM vehicle_function_domain_video v
LEFT JOIN vehicle_functional_domain vd ON v.function_domain_id = vd.id
LEFT JOIN vehicle_domain_index vi ON v.function_domain_index_id = vi.id
LIMIT 10;

-- 9. 查看视频分析状态
SELECT '视频分析状态...' AS step;
SELECT 
    v.id AS video_id,
    v.task_type,
    CASE WHEN r.id IS NOT NULL THEN '已分析' ELSE '未分析' END AS analysis_status,
    r.overall_score,
    r.summary
FROM vehicle_function_domain_video v
LEFT JOIN video_analysis_result r ON v.id = r.video_id
LIMIT 10;

-- 10. 推荐的测试视频ID
SELECT '推荐的测试视频ID...' AS step;
SELECT 
    id AS recommended_video_id,
    task_type,
    description
FROM vehicle_function_domain_video
WHERE video_url IS NOT NULL AND video_url != ''
LIMIT 5;
