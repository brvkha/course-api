# ==========================================
# STAGE 1: BUILD IMAGE
# ==========================================
# Sử dụng image Maven chính thức có chứa sẵn JDK 17 (hoặc 21 tùy bạn)
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Đặt thư mục làm việc bên trong container
WORKDIR /app

# Copy file pom.xml vào trước để tải dependencies
# (Mẹo DevOps: Việc này giúp tận dụng cache của Docker. Nếu pom.xml không đổi, Docker sẽ không tải lại thư viện ở những lần build sau)
COPY pom.xml .

# Tải trước các thư viện (offline)
RUN mvn dependency:go-offline -B

# Copy toàn bộ mã nguồn vào container
COPY src ./src

# Build ra file .jar (bỏ qua chạy Unit Test để build nhanh hơn trong lúc demo)
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: RUNTIME IMAGE
# ==========================================
# Sử dụng JRE siêu nhẹ (Alpine) để giảm kích thước image, tiết kiệm chi phí lưu trữ trên Cloud
FROM eclipse-temurin:21-jre-alpine

# Cập nhật múi giờ (Timezone) cho container về giờ Việt Nam (quan trọng khi ghi log)
RUN apk add --no-cache tzdata
ENV TZ=Asia/Ho_Chi_Minh

WORKDIR /app

# Khai báo port mà ứng dụng sẽ chạy (chỉ mang tính chất document)
EXPOSE 8080

# Chép file .jar từ STAGE 1 (builder) sang STAGE 2
COPY --from=builder /app/target/*.jar app.jar

# Chỗ này chừa sẵn đường để sau này mount file agent của New Relic và Splunk
# RUN mkdir -p /opt/newrelic /opt/splunk

# Lệnh khởi chạy ứng dụng khi container start
ENTRYPOINT ["java", "-jar", "app.jar"]