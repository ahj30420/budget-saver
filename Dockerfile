# 1. 빌드 스테이지 (Gradle + JDK 21)
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

# Gradle 캐시 활용
COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN gradle dependencies --no-daemon || true

# 소스 복사 후 빌드
COPY . .
RUN gradle clean build -x test --no-daemon

# 2. 런타임 스테이지 (JDK 21 JRE)
FROM eclipse-temurin:21-jre
WORKDIR /app

# 빌드 결과 복사 (JAR)
COPY --from=builder /app/build/libs/*.jar app.jar

# 컨테이너에서 사용할 포트
EXPOSE 8080

# prod 프로파일 기본 설정
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
