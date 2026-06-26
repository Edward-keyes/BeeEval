-- PGVector扩展安装和表结构
-- 用于存储视频分析结果的向量表示

-- 1. 启用PGVector扩展
CREATE EXTENSION IF NOT EXISTS vector;

-- 2. 创建视频向量存储表
CREATE TABLE IF NOT EXISTS video_vector_store (
    id BIGSERIAL PRIMARY KEY,
    video_id BIGINT NOT NULL COMMENT '视频ID',
    vehicle_id BIGINT COMMENT '车辆ID',
    function_domain_id BIGINT COMMENT '功能域ID',
    function_domain_index_id BIGINT COMMENT '三级指标ID',
    
    -- 文本内容
    summary_text TEXT COMMENT '视频摘要文本',
    transcript_text TEXT COMMENT '转录文本',
    analysis_text TEXT COMMENT '分析评分文本',
    keyframe_text TEXT COMMENT '关键帧描述文本',
    
    -- 向量嵌入 (使用通义千问embedding，维度为1536)
    summary_embedding vector(1536) COMMENT '摘要向量',
    transcript_embedding vector(1536) COMMENT '转录向量',
    analysis_embedding vector(1536) COMMENT '分析向量',
    keyframe_embedding vector(1536) COMMENT '关键帧向量',
    
    -- 元数据
    video_type INT COMMENT '视频类型：1-优秀案例，2-问题案例',
    task_type VARCHAR(100) COMMENT '任务类型',
    overall_score DECIMAL(3,2) COMMENT '综合评分',
    
    -- 时间戳
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- 索引
    UNIQUE KEY uk_video_id (video_id),
    INDEX idx_vehicle_id (vehicle_id),
    INDEX idx_function_domain_id (function_domain_id),
    INDEX idx_video_type (video_type)
);

-- 3. 创建向量索引（使用IVFFlat索引，适合中等规模数据）
-- 注意：需要在数据导入后创建索引
-- CREATE INDEX IF NOT EXISTS idx_summary_embedding ON video_vector_store 
--     USING ivfflat (summary_embedding vector_cosine_ops) WITH (lists = 100);

-- 4. 创建全文搜索索引（用于混合检索）
CREATE INDEX IF NOT EXISTS idx_summary_fts ON video_vector_store USING gin(to_tsvector('chinese', summary_text));
CREATE INDEX IF NOT EXISTS idx_transcript_fts ON video_vector_store USING gin(to_tsvector('chinese', transcript_text));

-- 5. 创建向量相似度搜索函数
CREATE OR REPLACE FUNCTION search_similar_videos(
    query_embedding vector(1536),
    match_threshold FLOAT DEFAULT 0.7,
    match_count INT DEFAULT 10
)
RETURNS TABLE (
    video_id BIGINT,
    vehicle_id BIGINT,
    function_domain_id BIGINT,
    summary_text TEXT,
    overall_score DECIMAL,
    similarity FLOAT
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT 
        v.video_id,
        v.vehicle_id,
        v.function_domain_id,
        v.summary_text,
        v.overall_score,
        1 - (v.summary_embedding <=> query_embedding) AS similarity
    FROM video_vector_store v
    WHERE 1 - (v.summary_embedding <=> query_embedding) > match_threshold
    ORDER BY v.summary_embedding <=> query_embedding
    LIMIT match_count;
END;
$$;

-- 6. 创建混合搜索函数（向量 + 全文）
CREATE OR REPLACE FUNCTION hybrid_search_videos(
    query_text TEXT,
    query_embedding vector(1536),
    match_threshold FLOAT DEFAULT 0.5,
    match_count INT DEFAULT 10
)
RETURNS TABLE (
    video_id BIGINT,
    vehicle_id BIGINT,
    function_domain_id BIGINT,
    summary_text TEXT,
    overall_score DECIMAL,
    vector_similarity FLOAT,
    text_rank FLOAT,
    combined_score FLOAT
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT 
        v.video_id,
        v.vehicle_id,
        v.function_domain_id,
        v.summary_text,
        v.overall_score,
        (1 - (v.summary_embedding <=> query_embedding)) AS vector_similarity,
        ts_rank_cd(to_tsvector('chinese', v.summary_text), plainto_tsquery('chinese', query_text)) AS text_rank,
        -- 组合分数：向量相似度权重0.7，全文搜索权重0.3
        (0.7 * (1 - (v.summary_embedding <=> query_embedding)) + 
         0.3 * ts_rank_cd(to_tsvector('chinese', v.summary_text), plainto_tsquery('chinese', query_text))) AS combined_score
    FROM video_vector_store v
    WHERE 
        (1 - (v.summary_embedding <=> query_embedding)) > match_threshold
        OR to_tsvector('chinese', v.summary_text) @@ plainto_tsquery('chinese', query_text)
    ORDER BY combined_score DESC
    LIMIT match_count;
END;
$$;

-- 7. 创建按车辆和功能域过滤的搜索函数
CREATE OR REPLACE FUNCTION search_videos_by_context(
    query_embedding vector(1536),
    p_vehicle_id BIGINT DEFAULT NULL,
    p_function_domain_id BIGINT DEFAULT NULL,
    p_video_type INT DEFAULT NULL,
    match_threshold FLOAT DEFAULT 0.5,
    match_count INT DEFAULT 10
)
RETURNS TABLE (
    video_id BIGINT,
    vehicle_id BIGINT,
    function_domain_id BIGINT,
    summary_text TEXT,
    overall_score DECIMAL,
    similarity FLOAT
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT 
        v.video_id,
        v.vehicle_id,
        v.function_domain_id,
        v.summary_text,
        v.overall_score,
        1 - (v.summary_embedding <=> query_embedding) AS similarity
    FROM video_vector_store v
    WHERE 
        (1 - (v.summary_embedding <=> query_embedding)) > match_threshold
        AND (p_vehicle_id IS NULL OR v.vehicle_id = p_vehicle_id)
        AND (p_function_domain_id IS NULL OR v.function_domain_id = p_function_domain_id)
        AND (p_video_type IS NULL OR v.video_type = p_video_type)
    ORDER BY v.summary_embedding <=> query_embedding
    LIMIT match_count;
END;
$$;

-- 8. 创建向量统计视图
CREATE OR REPLACE VIEW video_vector_stats AS
SELECT 
    COUNT(*) AS total_videos,
    COUNT(DISTINCT vehicle_id) AS total_vehicles,
    COUNT(DISTINCT function_domain_id) AS total_domains,
    COUNT(CASE WHEN video_type = 1 THEN 1 END) AS good_case_count,
    COUNT(CASE WHEN video_type = 2 THEN 1 END) AS bad_case_count,
    AVG(overall_score) AS avg_score,
    MAX(updated_at) AS last_updated
FROM video_vector_store;

-- 9. 创建未向量化视频视图
CREATE OR REPLACE VIEW unprocessed_videos AS
SELECT 
    v.id AS video_id,
    v.vehicle_id,
    v.function_domain_id,
    v.function_domain_index_id,
    v.type AS video_type,
    v.task_type,
    v.description
FROM vehicle_function_domain_video v
LEFT JOIN video_vector_store vs ON v.id = vs.video_id
WHERE vs.id IS NULL;

-- 10. 创建更新时间触发器
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_video_vector_store_updated_at 
    BEFORE UPDATE ON video_vector_store 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();
