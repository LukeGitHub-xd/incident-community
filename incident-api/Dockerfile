# 使用公司级 Java 21 JDK 基础镜像
FROM openjdk21-alpine-base:latest

LABEL service="incident-api"

WORKDIR /app

# 设置工作目录并复制 JAR 文件
WORKDIR /app
COPY target/*.jar /app/app.jar

# 暴露端口
EXPOSE 8024

# 业务 JVM 默认参数（可被运行时覆盖）
ENV JAVA_OPTS="-Xms512m -Xmx512m -Dio.netty.transport.noNative=true -Dio.netty.handler.ssl.noOpenSsl=true"

# Spring Profile 默认值
ENV SPRING_PROFILES_ACTIVE=default

# 使用 CMD（不要写 ENTRYPOINT）
CMD ["sh", "-c", "java -jar $JAVA_OPTS /app/app.jar --spring.profiles.active=$SPRING_PROFILES_ACTIVE"]