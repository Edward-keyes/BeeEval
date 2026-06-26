@echo off
chcp 65001 >nul
echo =========================================
echo 视频向量数据库初始化脚本
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

echo =========================================
echo 步骤2: 准备数据库
echo =========================================
echo.

set /p DB_NAME="请输入数据库名称: "
set /p DB_USER="请输入数据库用户名 [postgres]: "
if "%DB_USER%"=="" set DB_USER=postgres

echo.
echo 请手动执行以下SQL脚本:
echo 1. 安装PGVector扩展:
echo    psql -U %DB_USER% -d %DB_NAME% -c "CREATE EXTENSION IF NOT EXISTS vector;"
echo.
echo 2. 创建表结构:
echo    psql -U %DB_USER% -d %DB_NAME% -f src\main\resources\sql\pgvector_schema.sql
echo.

pause

echo.
echo =========================================
echo 步骤3: 启动应用
echo =========================================
echo.

echo 启动Spring Boot应用...
start /B mvn spring-boot:run

echo 等待应用启动...
timeout /t 30 /nobreak >nul

echo 应用已启动
echo.

echo =========================================
echo 步骤4: 执行批量向量化
echo =========================================
echo.

set /p CONFIRM="是否开始批量向量化? (y/n): "
if /i "%CONFIRM%"=="y" (
    echo 开始批量向量化...
    
    curl -s -X POST http://localhost:8080/api/v1/video-vectorization/batch-async ^
      -H "Content-Type: application/json" ^
      -d "{}"
    
    echo.
    echo 任务已提交，请使用返回的taskId查询进度
    echo 查询命令:
    echo curl -X GET http://localhost:8080/api/v1/video-vectorization/status/{taskId}
)

echo.

echo =========================================
echo 步骤5: 创建向量索引
echo =========================================
echo.

echo 数据导入完成后，请手动创建向量索引:
echo psql -U %DB_USER% -d %DB_NAME% -c "CREATE INDEX IF NOT EXISTS idx_summary_embedding ON video_vector_store USING ivfflat (summary_embedding vector_cosine_ops) WITH (lists = 100);"
echo.

pause

echo.
echo =========================================
echo 初始化完成！
echo =========================================
echo.
echo 查看使用指南: docs\视频向量数据库使用指南.md
echo.
echo 按任意键停止应用...
pause >nul

echo 停止应用...
taskkill /F /IM java.exe >nul 2>&1

echo 初始化脚本结束
