-- 智能问答系统数据库初始化脚本
-- 执行前请确保数据库已创建

-- 1. 问答会话表
CREATE TABLE IF NOT EXISTS qa_session (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    session_id VARCHAR(64) NOT NULL COMMENT '会话唯一标识',
    user_id VARCHAR(64) NOT NULL COMMENT '用户ID',
    user_name VARCHAR(100) COMMENT '用户名',
    status ENUM('active', 'inactive', 'expired') DEFAULT 'active' COMMENT '会话状态',
    last_activity_time DATETIME COMMENT '最后活动时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_session_id (session_id)
) COMMENT '问答会话表';

-- 2. 查询历史表
CREATE TABLE IF NOT EXISTS qa_query_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    session_id VARCHAR(64) NOT NULL COMMENT '会话ID',
    user_id VARCHAR(64) NOT NULL COMMENT '用户ID',
    question TEXT NOT NULL COMMENT '用户问题',
    question_embedding TEXT COMMENT '问题向量表示',
    generated_sql TEXT COMMENT '生成的SQL语句',
    sql_result JSON COMMENT 'SQL查询结果',
    answer TEXT COMMENT '生成的答案',
    answer_embedding TEXT COMMENT '答案向量表示',
    template_id BIGINT COMMENT '使用的模板ID',
    visualization_type VARCHAR(50) COMMENT '可视化类型',
    visualization_config JSON COMMENT '可视化配置',
    status ENUM('success', 'failed', 'processing') DEFAULT 'processing' COMMENT '查询状态',
    error_message TEXT COMMENT '错误信息',
    response_time INT COMMENT '响应时间(毫秒)',
    is_cache_hit TINYINT(1) DEFAULT 0 COMMENT '是否缓存命中',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY idx_session_id (session_id),
    KEY idx_user_id (user_id),
    KEY idx_create_time (create_time)
) COMMENT '查询历史表';

-- 3. 查询审计表
CREATE TABLE IF NOT EXISTS qa_audit_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id VARCHAR(64) NOT NULL COMMENT '用户ID',
    user_name VARCHAR(100) COMMENT '用户名',
    session_id VARCHAR(64) COMMENT '会话ID',
    query_id BIGINT COMMENT '查询ID',
    question TEXT NOT NULL COMMENT '问题内容',
    sql_executed TEXT COMMENT '执行的SQL',
    result_count INT DEFAULT 0 COMMENT '结果条数',
    ip_address VARCHAR(45) COMMENT 'IP地址',
    user_agent TEXT COMMENT '用户代理',
    device_info VARCHAR(200) COMMENT '设备信息',
    access_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    response_time INT COMMENT '响应时间(毫秒)',
    is_sensitive TINYINT(1) DEFAULT 0 COMMENT '是否涉及敏感数据',
    risk_level ENUM('low', 'medium', 'high') DEFAULT 'low' COMMENT '风险等级',
    KEY idx_user_id (user_id),
    KEY idx_access_time (access_time),
    KEY idx_ip_address (ip_address)
) COMMENT '查询审计表';

-- 4. 问题模板表
CREATE TABLE IF NOT EXISTS qa_question_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(200) NOT NULL COMMENT '模板名称',
    description TEXT COMMENT '模板描述',
    category VARCHAR(100) COMMENT '分类',
    question_pattern TEXT COMMENT '问题匹配模式(正则)',
    keywords JSON COMMENT '关键词列表',
    sql_template TEXT COMMENT 'SQL模板',
    parameters JSON COMMENT '参数配置',
    visualization_type VARCHAR(50) COMMENT '默认可视化类型',
    visualization_config JSON COMMENT '可视化配置',
    priority INT DEFAULT 0 COMMENT '优先级',
    is_active TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    hit_count INT DEFAULT 0 COMMENT '使用次数',
    create_user VARCHAR(64) COMMENT '创建人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_category (category),
    KEY idx_is_active (is_active)
) COMMENT '问题模板表';

-- 5. 可视化模板表
CREATE TABLE IF NOT EXISTS qa_visualization_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name VARCHAR(200) NOT NULL COMMENT '模板名称',
    type ENUM('table', 'bar', 'line', 'pie', 'scatter', 'radar', 'heatmap') NOT NULL COMMENT '图表类型',
    config JSON NOT NULL COMMENT '图表配置',
    description TEXT COMMENT '描述',
    category VARCHAR(100) COMMENT '分类',
    is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认',
    is_active TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_name (name)
) COMMENT '可视化模板表';

-- 6. 缓存表
CREATE TABLE IF NOT EXISTS qa_cache (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    cache_key VARCHAR(128) NOT NULL COMMENT '缓存键(MD5)',
    question_hash VARCHAR(64) NOT NULL COMMENT '问题哈希',
    question TEXT NOT NULL COMMENT '原始问题',
    answer TEXT COMMENT '答案内容',
    sql_result JSON COMMENT 'SQL结果',
    visualization_data JSON COMMENT '可视化数据',
    expire_time DATETIME NOT NULL COMMENT '过期时间',
    hit_count INT DEFAULT 0 COMMENT '命中次数',
    last_access_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后访问时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_cache_key (cache_key),
    KEY idx_expire_time (expire_time),
    KEY idx_hit_count (hit_count)
) COMMENT '缓存表';

-- 7. 知识库文档表
CREATE TABLE IF NOT EXISTS qa_knowledge_base (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    title VARCHAR(500) NOT NULL COMMENT '文档标题',
    content TEXT NOT NULL COMMENT '文档内容',
    content_embedding TEXT COMMENT '内容向量表示',
    category VARCHAR(100) COMMENT '分类',
    tags JSON COMMENT '标签',
    source VARCHAR(200) COMMENT '来源',
    author VARCHAR(100) COMMENT '作者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_category (category),
    FULLTEXT KEY idx_content (content)
) COMMENT '知识库文档表';

-- 初始化基础数据
INSERT IGNORE INTO qa_visualization_template (name, type, config, description, category, is_default) VALUES
('默认表格', 'table', '{"pagination": true, "pageSize": 20, "sortable": true}', '默认表格视图', 'general', 1),
('柱状图', 'bar', '{"xAxis": {"type": "category"}, "yAxis": {"type": "value"}, "legend": {"show": true}}', '适用于分类对比数据', 'chart', 0),
('线图', 'line', '{"xAxis": {"type": "category"}, "yAxis": {"type": "value"}, "smooth": true}', '适用于趋势展示', 'chart', 0),
('饼图', 'pie', '{"legend": {"show": true}, "label": {"show": true}}', '适用于占比展示', 'chart', 0);

-- 初始化基础问题模板
INSERT IGNORE INTO qa_question_template (name, description, category, keywords, sql_template, visualization_type, priority, is_active) VALUES
('车辆功能域分数查询', '查询车辆的功能域评估分数', 'vehicle_score', '["车辆", "分数", "功能域", "评估"]', 'SELECT vehicle_id, domain_id, score, type FROM vehicle_domain_score WHERE vehicle_id = ? AND type = 1', 'table', 10, 1),
('车辆指标分数查询', '查询车辆的指标评估分数', 'vehicle_score', '["车辆", "指标", "分数", "评估"]', 'SELECT vehicle_id, domain_index_id, score FROM vehicle_domain_score WHERE vehicle_id = ? AND type = 2', 'bar', 10, 1),
('功能域平均分查询', '查询功能域的平均分数', 'statistics', '["平均分", "功能域", "统计"]', 'SELECT domain_id, AVG(score) as avg_score FROM vehicle_domain_score WHERE type = 1 GROUP BY domain_id', 'bar', 8, 1);
