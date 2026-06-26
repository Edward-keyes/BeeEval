# 视频分析RAG系统验证指南

## 一、环境准备

### 1. 必备软件
- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+
- FFmpeg（用于视频处理）

### 2. FFmpeg安装
```bash
# Windows (使用Chocolatey)
choco install ffmpeg

# 或者下载FFmpeg并配置环境变量
# 下载地址: https://ffmpeg.org/download.html

# 验证安装
ffmpeg -version
```

### 3. 配置检查
确保以下配置正确：
- Nacos中的通义千问API Key配置
- OSS访问配置（AccessKey、SecretKey、Bucket、Endpoint）
- 数据库连接配置
- Redis连接配置

## 二、数据准备

### 1. 执行数据准备脚本
```bash
# 连接数据库并执行
mysql -u root -p your_database < src/test/resources/test_data_preparation.sql
```

### 2. 检查数据完整性
确保以下表有数据：
- `vehicle` - 车辆信息
- `vehicle_functional_domain` - 功能域
- `vehicle_domain_index` - 三级指标
- `vehicle_function_domain_video` - 视频数据
- `vehicle_domain_score` - 得分数据

### 3. 同步知识库
```bash
# 运行测试同步车辆数据到知识库
mvn test -Dtest=QaSystemTest#testVehicleDataSync
```

## 三、单元测试验证

### 1. 运行所有测试
```bash
# 运行所有测试
mvn test

# 只运行视频分析相关测试
mvn test -Dtest=VideoAnalysisTest
```

### 2. 运行特定测试
```bash
# 测试视频元数据提取
mvn test -Dtest=VideoAnalysisTest#testVideoMetadataExtraction

# 测试音频提取
mvn test -Dtest=VideoAnalysisTest#testAudioExtraction

# 测试关键帧截取
mvn test -Dtest=VideoAnalysisTest#testFrameCapture

# 测试语音转文字
mvn test -Dtest=VideoAnalysisTest#testAudioTranscription

# 测试图像理解
mvn test -Dtest=VideoAnalysisTest#testImageUnderstanding

# 测试视频分析
mvn test -Dtest=VideoAnalysisTest#testVideoAnalysis

# 测试异步视频分析
mvn test -Dtest=VideoAnalysisTest#testVideoAnalysisAsync

# 测试智能问答集成
mvn test -Dtest=VideoAnalysisTest#testQaWithVideoAnalysis
```

### 3. 使用自定义测试文件
```bash
# 指定测试视频路径
mvn test -Dtest=VideoAnalysisTest#testVideoMetadataExtraction \
  -Dtest.video.path=/path/to/test.mp4

# 指定测试音频路径
mvn test -Dtest=VideoAnalysisTest#testAudioTranscription \
  -Dtest.audio.path=/path/to/test.mp3

# 指定测试图像路径
mvn test -Dtest=VideoAnalysisTest#testImageUnderstanding \
  -Dtest.image.path=/path/to/test.jpg

# 指定测试视频ID
mvn test -Dtest=VideoAnalysisTest#testVideoAnalysisAsync \
  -Dtest.video.id=123
```

## 四、API接口测试

### 1. 启动应用
```bash
mvn spring-boot:run
```

### 2. 创建会话
```bash
curl -X POST http://localhost:8080/api/v1/qa/session/create \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "test_user",
    "userName": "测试用户"
  }'
```

记录返回的 `sessionId`。

### 3. 分析单个视频
```bash
curl -X POST http://localhost:8080/api/v1/video-analysis/analyze/1 \
  -H "Authorization: Bearer your_token"
```

### 4. 检查分析状态
```bash
curl -X GET http://localhost:8080/api/v1/video-analysis/status/{taskId} \
  -H "Authorization: Bearer your_token"
```

### 5. 获取分析结果
```bash
curl -X GET http://localhost:8080/api/v1/video-analysis/result/1 \
  -H "Authorization: Bearer your_token"
```

### 6. 批量分析视频
```bash
curl -X POST http://localhost:8080/api/v1/video-analysis/analyze/batch \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your_token" \
  -d '{
    "videoIds": [1, 2, 3]
  }'
```

### 7. 智能问答（带视频分析）
```bash
curl -X POST http://localhost:8080/api/v1/qa/query \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your_token" \
  -d '{
    "sessionId": "your_session_id",
    "userId": "test_user",
    "question": "查询车辆在智能驾驶功能域的视频表现如何？"
  }'
```

## 五、集成测试场景

### 场景1：完整视频分析流程
```bash
# 1. 创建会话
SESSION_ID=$(curl -s -X POST http://localhost:8080/api/v1/qa/session/create \
  -H "Content-Type: application/json" \
  -d '{"userId": "test_user", "userName": "测试用户"}' | jq -r '.sessionId')

echo "Session ID: $SESSION_ID"

# 2. 分析视频
TASK_ID=$(curl -s -X POST http://localhost:8080/api/v1/video-analysis/analyze/1 \
  -H "Authorization: Bearer your_token" | jq -r '.taskId')

echo "Task ID: $TASK_ID"

# 3. 等待分析完成（最多等待60秒）
for i in {1..60}; do
  STATUS=$(curl -s -X GET http://localhost:8080/api/v1/video-analysis/status/$TASK_ID \
    -H "Authorization: Bearer your_token" | jq -r '.status')
  
  echo "Status: $STATUS"
  
  if [ "$STATUS" = "completed" ]; then
    break
  elif [ "$STATUS" = "failed" ]; then
    echo "Analysis failed!"
    exit 1
  fi
  
  sleep 1
done

# 4. 获取分析结果
curl -X GET http://localhost:8080/api/v1/video-analysis/result/1 \
  -H "Authorization: Bearer your_token" | jq '.'

# 5. 查询视频相关问题
curl -X POST http://localhost:8080/api/v1/qa/query \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your_token" \
  -d "{
    \"sessionId\": \"$SESSION_ID\",
    \"userId\": \"test_user\",
    \"question\": \"分析车辆在车道保持测试中的视频表现\"
  }" | jq '.'
```

### 场景2：对比分析
```bash
# 1. 批量分析多个视频
curl -X POST http://localhost:8080/api/v1/video-analysis/analyze/batch \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your_token" \
  -d '{"videoIds": [1, 2, 3]}'

# 2. 等待分析完成
sleep 60

# 3. 查询对比分析
curl -X POST http://localhost:8080/api/v1/qa/query \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your_token" \
  -d '{
    "sessionId": "your_session_id",
    "userId": "test_user",
    "question": "对比分析车辆在不同功能域的视频表现差异"
  }'
```

## 六、验证检查点

### 1. 视频处理验证
- [ ] FFmpeg能正常提取音频
- [ ] FFmpeg能正常截取关键帧
- [ ] 视频元数据提取正确
- [ ] 临时文件能正常清理

### 2. 多模态分析验证
- [ ] 通义千问音频API能正常调用
- [ ] 语音转文字结果准确
- [ ] 通义千问视觉API能正常调用
- [ ] 图像理解结果合理
- [ ] 视频分析结果包含各维度评分

### 3. 数据存储验证
- [ ] 分析结果能正确保存到数据库
- [ ] 转录文本能正确保存
- [ ] 各维度评分能正确保存

### 4. 智能问答集成验证
- [ ] 能识别视频相关问题
- [ ] 能提取视频ID
- [ ] 能获取视频分析结果
- [ ] 能将视频分析结果注入Prompt
- [ ] 最终答案包含视频分析内容

### 5. 性能验证
- [ ] 单个视频分析时间在合理范围内（< 2分钟）
- [ ] 批量分析能正常并发执行
- [ ] 异步任务状态能正确更新
- [ ] 内存使用正常，无内存泄漏

## 七、常见问题排查

### 1. FFmpeg相关错误
```bash
# 检查FFmpeg是否安装
ffmpeg -version

# 检查FFmpeg路径
where ffmpeg  # Windows
which ffmpeg  # Linux/Mac
```

### 2. OSS访问错误
```bash
# 检查OSS配置
# 确保AccessKey、SecretKey、Bucket、Endpoint正确
# 确保网络能访问OSS服务
```

### 3. 通义千问API错误
```bash
# 检查API Key配置
# 检查API调用限制
# 检查模型名称是否正确（qwen-vl-max, qwen-audio-turbo）
```

### 4. 数据库错误
```bash
# 检查表是否存在
SHOW TABLES LIKE 'video_analysis_result';

# 检查字段是否正确
DESC video_analysis_result;
```

### 5. 异步任务错误
```bash
# 检查线程池配置
# 检查日志中的异常信息
# 检查Redis连接状态
```

## 八、测试报告生成

### 1. 运行测试并生成报告
```bash
mvn test -Dtest=VideoAnalysisTest -Dsurefire.useFile=false
```

### 2. 查看测试覆盖率
```bash
mvn jacoco:report
```

### 3. 查看测试结果
测试结果位于：
- `target/surefire-reports/` - 测试报告
- `target/site/jacoco/` - 覆盖率报告

## 九、性能测试

### 1. 单视频分析性能
```bash
# 测试单个视频分析时间
time mvn test -Dtest=VideoAnalysisTest#testVideoAnalysisAsync
```

### 2. 批量分析性能
```bash
# 测试批量分析性能
time mvn test -Dtest=VideoAnalysisTest#testBatchVideoAnalysis
```

### 3. 并发查询性能
使用JMeter或其他压测工具进行并发测试。

## 十、验收标准

### 必须通过的测试
1. ✅ 视频元数据提取正常
2. ✅ 音频提取正常
3. ✅ 关键帧截取正常
4. ✅ 语音转文字正常
5. ✅ 视频分析结果合理
6. ✅ 分析结果能正确保存
7. ✅ 智能问答能集成视频分析
8. ✅ 异步任务状态正确更新

### 性能标准
1. 单个视频分析时间 < 2分钟
2. 批量分析支持至少10个并发
3. 智能问答响应时间 < 5秒（不含视频分析）

### 准确性标准
1. 语音转文字准确率 > 80%
2. 视频分析评分合理性 > 90%
3. 智能问答答案相关性 > 85%
