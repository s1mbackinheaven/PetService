# # Sử dụng JDK 17 làm base image (phổ biến cho Spring Boot 3.x)
# FROM eclipse-temurin:17-jdk AS builder

# # Thiết lập thư mục làm việc
# WORKDIR /app

# # Sao chép toàn bộ dự án ngay từ đầu
# COPY . .

# # Cấp quyền thực thi cho mvnw
# RUN chmod +x ./mvnw

# # Build với thông tin đầy đủ về lỗi và bỏ qua tests
# RUN ./mvnw clean package -DskipTests --fail-fast -e

# # Sử dụng JRE nhẹ hơn cho runtime
# FROM eclipse-temurin:17-jre

# # Thiết lập thư mục làm việc
# WORKDIR /app

# # Sao chép file JAR đã build từ stage trước
# COPY --from=builder /app/target/*.jar app.jar

# # Mở cổng 8080 để truy cập API
# EXPOSE 8080

# # Thêm metadata cho container
# LABEL maintainer="PetService Team" \
#       description="PetService Backend Application" \
#       version="1.0"

# # Thiết lập biến môi trường cho Spring Boot
# ENV SPRING_PROFILES_ACTIVE=prod
# ENV PORT=8080
# ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# # Thiết lập điểm kiểm tra sức khỏe
# HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
#   CMD curl -f http://localhost:8080/actuator/health || exit 1

# # Câu lệnh chạy ứng dụng khi container khởi động
# ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"] 
# Stage 1: build
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

# Stage 2: create image
FROM openjdk:17-slim

WORKDIR /app
COPY --from=build /app/target/*.jar backend.jar

CMD ["java", "-jar", "backend.jar"]