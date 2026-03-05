package com.practice.course_api.repository;

import com.practice.course_api.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    // Có thể thêm các hàm query custom sau này, ví dụ: findByTitleContaining...
}