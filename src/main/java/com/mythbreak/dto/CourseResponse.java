package com.mythbreak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for a course.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String level;
    private String category;
    private String thumbnailUrl;
    private boolean published;
    private LocalDateTime createdAt;

    // Educator info (avoids circular reference to entity)
    private Long educatorId;
    private String educatorName;

    private List<CourseContentResponse> contents;
    private long enrolledCount;
}
