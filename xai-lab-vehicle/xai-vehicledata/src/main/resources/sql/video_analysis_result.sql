-- 视频分析结果表
CREATE TABLE IF NOT EXISTS video_analysis_result (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    video_id BIGINT NOT NULL COMMENT '关联vehicle_function_domain_video.id',
    vehicle_id BIGINT COMMENT '车辆ID',
    domain_id BIGINT COMMENT '功能域ID',
    index_id BIGINT COMMENT '三级指标ID',
    
    -- 音频转录
    transcript TEXT COMMENT '语音转文字结果',
    transcript_confidence DOUBLE COMMENT '转录置信度',
    
    -- 视觉分析
    screenshot_url VARCHAR(500) COMMENT '关键帧截图URL',
    visual_description TEXT COMMENT '视觉内容描述',
    
    -- AI评估结果
    evaluation_score DOUBLE COMMENT '评估得分',
    evaluation_dimensions JSON COMMENT '各维度评分JSON',
    evaluation_summary TEXT COMMENT '评估总结',
    improvement_suggestions TEXT COMMENT '改进建议',
    
    -- 元数据
    analysis_model VARCHAR(50) COMMENT '使用的模型',
    processing_time_ms INT COMMENT '处理耗时(毫秒)',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态: pending/processing/completed/failed',
    error_message TEXT COMMENT '错误信息',
    
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_video_id (video_id),
    INDEX idx_vehicle_id (vehicle_id),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频分析结果表';
