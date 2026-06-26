#!/bin/bash

echo "========================================="
echo "视频分析RAG系统快速验证脚本"
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

echo "检查FFmpeg..."
ffmpeg -version 2>&1 | head -n 1
echo ""

echo "检查MySQL连接..."
mysql -u root -p${DB_PASSWORD:-root} -e "SELECT 1" 2>&1 | head -n 1
echo ""

echo "检查Redis连接..."
redis-cli ping 2>&1 | head -n 1
echo ""

echo "========================================="
echo "步骤2: 准备测试数据..."
echo ""

echo "执行数据准备脚本..."
mysql -u root -p${DB_PASSWORD:-root} ${DB_NAME:-xai_vehicle} < src/test/resources/test_data_preparation.sql
echo ""

echo "========================================="
echo "步骤3: 运行单元测试..."
echo ""

echo "运行基础测试..."
mvn test -Dtest=QaSystemTest -DfailIfNoTests=false
echo ""

echo "运行视频分析测试..."
mvn test -Dtest=VideoAnalysisTest -DfailIfNoTests=false
echo ""

echo "========================================="
echo "步骤4: 启动应用..."
echo ""

echo "启动Spring Boot应用..."
mvn spring-boot:run &
APP_PID=$!

echo "等待应用启动..."
sleep 30

echo "应用已启动，PID: $APP_PID"
echo ""

echo "========================================="
echo "步骤5: API接口测试..."
echo ""

echo "测试1: 创建会话"
SESSION_RESPONSE=$(curl -s -X POST http://localhost:8080/api/v1/qa/session/create \
  -H "Content-Type: application/json" \
  -d '{"userId": "test_user", "userName": "测试用户"}')
echo "$SESSION_RESPONSE"
SESSION_ID=$(echo $SESSION_RESPONSE | grep -o '"sessionId":"[^"]*"' | cut -d'"' -f4)
echo "Session ID: $SESSION_ID"
echo ""

echo "测试2: 查询车辆信息"
curl -s -X POST http://localhost:8080/api/v1/qa/query \
  -H "Content-Type: application/json" \
  -d "{
    \"sessionId\": \"$SESSION_ID\",
    \"userId\": \"test_user\",
    \"question\": \"查询所有功能域的平均评分\"
  }" | jq '.'
echo ""

echo "测试3: 检查视频数据"
curl -s -X GET http://localhost:8080/api/v1/video-analysis/result/1 | jq '.'
echo ""

echo "========================================="
echo "验证完成！"
echo "========================================="
echo ""
echo "应用仍在运行，PID: $APP_PID"
echo "按Ctrl+C停止应用"
echo ""
echo "访问以下地址进行测试："
echo "- API文档: http://localhost:8080/swagger-ui.html"
echo "- 健康检查: http://localhost:8080/actuator/health"
echo ""

wait $APP_PID
