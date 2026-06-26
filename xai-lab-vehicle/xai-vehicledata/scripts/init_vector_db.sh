#!/bin/bash

echo "========================================="
echo "视频向量数据库初始化脚本"
echo "========================================="
echo ""

echo "步骤1: 检查环境..."
echo ""

echo "检查Java版本..."
java -version
echo ""

echo "检查Maven版本..."
mvn -version
echo ""

echo "检查PostgreSQL连接..."
psql -U postgres -c "SELECT version();" 2>&1 | head -n 1
echo ""

echo "========================================="
echo "步骤2: 安装PGVector扩展..."
echo ""

read -p "请输入数据库名称: " DB_NAME
read -p "请输入数据库用户名 [postgres]: " DB_USER
DB_USER=${DB_USER:-postgres}

echo "安装PGVector扩展..."
psql -U $DB_USER -d $DB_NAME -c "CREATE EXTENSION IF NOT EXISTS vector;"
echo ""

echo "========================================="
echo "步骤3: 创建表结构..."
echo ""

psql -U $DB_USER -d $DB_NAME -f src/main/resources/sql/pgvector_schema.sql
echo ""

echo "========================================="
echo "步骤4: 验证表结构..."
echo ""

psql -U $DB_USER -d $DB_NAME -c "\d video_vector_store"
echo ""

echo "========================================="
echo "步骤5: 检查视频数据..."
echo ""

VIDEO_COUNT=$(psql -U $DB_USER -d $DB_NAME -t -c "SELECT COUNT(*) FROM vehicle_function_domain_video;")
echo "视频总数: $VIDEO_COUNT"
echo ""

ANALYSIS_COUNT=$(psql -U $DB_USER -d $DB_NAME -t -c "SELECT COUNT(*) FROM video_analysis_result;")
echo "已分析视频数: $ANALYSIS_COUNT"
echo ""

echo "========================================="
echo "步骤6: 启动应用..."
echo ""

echo "启动Spring Boot应用..."
mvn spring-boot:run &
APP_PID=$!

echo "等待应用启动..."
sleep 30

echo "应用已启动，PID: $APP_PID"
echo ""

echo "========================================="
echo "步骤7: 执行批量向量化..."
echo ""

read -p "是否开始批量向量化? (y/n): " CONFIRM
if [ "$CONFIRM" = "y" ]; then
    echo "开始批量向量化..."
    
    RESPONSE=$(curl -s -X POST http://localhost:8080/api/v1/video-vectorization/batch-async \
      -H "Content-Type: application/json" \
      -d '{}')
    
    TASK_ID=$(echo $RESPONSE | grep -o '"taskId":"[^"]*"' | cut -d'"' -f4)
    
    echo "任务已提交，TaskID: $TASK_ID"
    echo ""
    
    echo "监控进度..."
    while true; do
        STATUS=$(curl -s -X GET http://localhost:8080/api/v1/video-vectorization/status/$TASK_ID)
        echo "$STATUS" | jq '.'
        
        IS_COMPLETED=$(echo $STATUS | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
        
        if [ "$IS_COMPLETED" = "completed" ] || [ "$IS_COMPLETED" = "failed" ]; then
            break
        fi
        
        sleep 10
    done
fi

echo ""

echo "========================================="
echo "步骤8: 创建向量索引..."
echo ""

read -p "是否创建向量索引? (y/n): " CONFIRM
if [ "$CONFIRM" = "y" ]; then
    echo "创建向量索引..."
    psql -U $DB_USER -d $DB_NAME -c "
        CREATE INDEX IF NOT EXISTS idx_summary_embedding ON video_vector_store 
        USING ivfflat (summary_embedding vector_cosine_ops) 
        WITH (lists = 100);
    "
    echo "索引创建完成"
fi

echo ""

echo "========================================="
echo "步骤9: 验证结果..."
echo ""

psql -U $DB_USER -d $DB_NAME -c "SELECT * FROM video_vector_stats;"
echo ""

echo "========================================="
echo "初始化完成！"
echo "========================================="
echo ""
echo "应用仍在运行，PID: $APP_PID"
echo "按Ctrl+C停止应用"
echo ""
echo "查看使用指南: docs/视频向量数据库使用指南.md"
echo ""

wait $APP_PID
