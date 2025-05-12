# Sử dụng JDK 21 làm base image
FROM eclipse-temurin:21-jdk AS builder

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép mã nguồn dự án
COPY . .

# Build ứng dụng Spring Boot với Maven
# Bỏ qua tests để có thể build nhanh hơn
RUN ./mvnw clean package -DskipTests

# Sử dụng JRE nhẹ hơn cho runtime
FROM eclipse-temurin:21-jre

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file JAR đã build từ stage trước
COPY --from=builder /app/target/*.jar app.jar

# Mở cổng 8080 để truy cập API
EXPOSE 8080

# Thiết lập biến môi trường
ENV SPRING_PROFILES_ACTIVE=prod

# Câu lệnh chạy ứng dụng khi container khởi động
ENTRYPOINT ["java", "-jar", "app.jar"] 