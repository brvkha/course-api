# ==========================================
# STAGE 1: BUILD IMAGE (Dùng Node.js)
# ==========================================
FROM node:18-alpine as builder

WORKDIR /app

# Copy package.json và cài đặt thư viện trước để tận dụng cache
COPY package*.json ./
RUN npm install

# Copy toàn bộ code và tiến hành build
COPY . .
# Lưu ý: Nếu bạn dùng Vite, thư mục build ra sẽ tên là 'dist'. 
# Nếu dùng Create React App, nó sẽ tên là 'build'. Ở đây mình dùng Vite theo các bước trước.
RUN npm run build

# ==========================================
# STAGE 2: RUNTIME IMAGE (Dùng Nginx)
# ==========================================
FROM nginx:alpine

# Xóa cấu hình Nginx mặc định
RUN rm /etc/nginx/conf.d/default.conf

# Copy cấu hình Nginx custom của mình vào
COPY nginx.conf /etc/nginx/conf.d

# Copy các file tĩnh đã được build từ STAGE 1 sang thư mục html của Nginx
COPY --from=builder /app/dist /usr/share/nginx/html

# Mở port 80 (Port mặc định của Web)
EXPOSE 80

# Lệnh khởi chạy Nginx
CMD ["nginx", "-g", "daemon off;"]