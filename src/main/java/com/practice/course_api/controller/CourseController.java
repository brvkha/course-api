package com.practice.course_api.controller;

import com.practice.course_api.dto.CourseRequest;
import com.practice.course_api.dto.CourseResponse;
import com.practice.course_api.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Course Management", description = "Các API quản lý khóa học")
@SecurityRequirement(name = "bearerAuth") // Yêu cầu Swagger gắn Token cho toàn bộ API trong Controller này
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Lấy danh sách tất cả khóa học")
    @GetMapping
    // API này không dùng @PreAuthorize, nghĩa là ai có Token hợp lệ (bất kể Role gì) cũng gọi được (do cấu hình anyRequest().authenticated() ở SecurityConfig)
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @Operation(summary = "Tạo mới khóa học")
    @PostMapping
    // ĐIỂM SÁNG GIÁ NHẤT: Chỉ ai có Role TRAINER hoặc ADMIN mới được phép gọi hàm này
    @PreAuthorize("hasAnyRole('TRAINER', 'ADMIN')")
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.createCourse(request));
    }
}