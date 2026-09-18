package com.mythbreak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for a single course content module.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseContentResponse {

    private Long id;
    private String title;
    private String description;
    private String contentUrl;
    private String contentType;
    private Integer sequenceOrder;
    private Integer durationMinutes;
    private Long courseId;
}
