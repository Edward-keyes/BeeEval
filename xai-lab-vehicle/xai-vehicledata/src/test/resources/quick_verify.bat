@echo off
chcp 65001 >nul
echo =========================================
echo 视频分析RAG系统快速验证脚本
echo =========================================
echo.

echo 步骤1: 检查环境...
echo.

echo 检查Java版本...
java -version
echo.

echo 检查Maven版本...
mvn -version
echo.

echo 检查FFmpeg...
ffmpeg -version 2>nul
if %errorlevel% neq 0 (
    echo 警告: FFmpeg未安装或未配置到PATH
)
echo.

echo =========================================
echo 步骤2: 运行单元测试...
echo.

echo 运行基础测试...
mvn test -Dtest=QaSystemTest -DfailIfNoTests=false
echo.

echo 运行视频分析测试...
mvn test -Dtest=VideoAnalysisTest -DfailIfNoTests=false
echo.

echo =========================================
echo 步骤3: 启动应用...
echo.

echo 启动Spring Boot应用...
start /B mvn spring-boot:run

echo 等待应用启动...
timeout /t 30 /nobreak >nul

echo 应用已启动
echo.

echo =========================================
echo 步骤4: API接口测试...
echo.

echo 测试1: 创建会话
curl -s -X POST http://localhost:8080/api/v1/qa/session/create ^
  -H "Content-Type: application/json" ^
  -d "{\"userId\": \"test_user\", \"userName\": \"测试用户\"}"
echo.
echo.

echo 测试2: 查询车辆信息
curl -s -X POST http://localhost:8080/api/v1/qa/query ^
  -H "Content-Type: application/json" ^
  -d "{\"sessionId\": \"test_session\", \"userId\": \"test_user\", \"question\": \"查询所有功能域的平均评分\"}"
echo.
echo.

echo =========================================
echo 验证完成！
echo =========================================
echo.
echo 应用仍在运行
echo 按任意键停止应用...
pause >nul

echo 停止应用...
taskkill /F /IM java.exe >nul 2>&1

echo 验证脚本结束
