package com.practice.course_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Course Management API",
                version = "1.0",
                description = "Tài liệu API cho dự án Demo Spring Boot & AWS",
                contact = @Contact(
                        name = "Backend Developer",
                        email = "dev@practice.com"
                )
        ),
        // Áp dụng yêu cầu bảo mật này cho TẤT CẢ các API hiển thị trên Swagger
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth", // Tên scheme này phải khớp với tên trong @SecurityRequirement ở trên
        description = "Nhập JWT token của bạn vào đây (Không cần thêm chữ Bearer).",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER // Token sẽ được truyền tự động vào Header Authorization
)
public class OpenApiConfig {
    // Không cần viết code logic gì ở đây, chỉ cần dùng Annotations là đủ.
}