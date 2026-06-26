# 使用 OpenJDK 21 作为基础镜像
FROM openjdk:21-jdk-slim

# 设置工作目录
WORKDIR /app

# 将打包好的 JAR 文件复制到容器中
COPY target/xai-vehicledata-0.0.1-SNAPSHOT.jar xai-vehicledata-0.0.1-SNAPSHOT.jar

# 暴露端口（根据你的微服务配置）
EXPOSE 8181

# 启动微服务
ENTRYPOINT ["java", "-jar", "xai-vehicledata-0.0.1-SNAPSHOT.jar"]