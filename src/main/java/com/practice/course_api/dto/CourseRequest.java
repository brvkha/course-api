package com.practice.course_api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CourseRequest {
    private String title;
    private String description;
    private BigDecimal price;
}