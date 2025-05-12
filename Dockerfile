# Sử dụng JDK 17 làm base image (phổ biến cho Spring Boot 3.x)
FROM eclipse-temurin:17-jdk AS builder

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép pom.xml riêng trước để tận dụng cache layer của Docker
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Cấp quyền thực thi cho mvnw
RUN chmod +x ./mvnw

# Tải các dependencies trước (sẽ được cache nếu pom.xml không thay đổi)
RUN ./mvnw dependency:go-offline -B

# Sao chép mã nguồn dự án
COPY src ./src

# Build ứng dụng Spring Boot với Maven
RUN ./mvnw package -DskipTests

# Sử dụng JRE nhẹ hơn cho runtime
FROM eclipse-temurin:17-jre

# Thiết lập thư mục làm việc
WORKDIR /app

# Tạo người dùng không phải root để chạy ứng dụng
RUN addgroup --system --gid 1001 petapp && \
    adduser --system --uid 1001 --gid 1001 petapp

# Sao chép file JAR đã build từ stage trước
COPY --from=builder /app/target/*.jar app.jar

# Chuyển quyền sở hữu file JAR cho người dùng không phải root
RUN chown -R petapp:petapp /app
USER petapp

# Mở cổng 8080 để truy cập API
EXPOSE 8080

# Thêm metadata cho container
LABEL maintainer="PetService Team" \
      description="PetService Backend Application" \
      version="1.0"

# Thiết lập biến môi trường cho Spring Boot
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# Thiết lập điểm kiểm tra sức khỏe
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Câu lệnh chạy ứng dụng khi container khởi động với cấu hình JVM tối ưu
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 