package com.practice.course_api.service;

import com.practice.course_api.dto.CourseRequest;
import com.practice.course_api.dto.CourseResponse;
import com.practice.course_api.entity.Course;
import com.practice.course_api.entity.User;
import com.practice.course_api.repository.CourseRepository;
import com.practice.course_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ActionLogService actionLogService;

    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        // 1. Lấy thông tin user đang đăng nhập từ Security Context
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        // 2. Tìm user trong DB
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin tác giả"));

        // 3. Map dữ liệu từ Request sang Entity
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .isPublished(false) // Mặc định tạo xong chưa public ngay
                .author(author)
                .build();

        // 4. Lưu vào DB
        Course savedCourse = courseRepository.save(course);

        actionLogService.logAction(
                username,
                "CREATE_COURSE",
                "Tạo khóa học mới: " + request.getTitle()
        );

        // 5. Map từ Entity sang Response trả về cho Frontend
        return mapToResponse(savedCourse);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Hàm helper để map Entity -> DTO
    private CourseResponse mapToResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .price(course.getPrice())
                .isPublished(course.getIsPublished())
                .authorName(course.getAuthor().getName())
                .build();
    }
}