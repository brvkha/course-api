package com.practice.course_api.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CourseResponse {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal price;
    private Boolean isPublished;
    private String authorName;
}