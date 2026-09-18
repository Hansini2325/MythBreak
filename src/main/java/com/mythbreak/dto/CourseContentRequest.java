package com.mythbreak.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Request body for creating / updating course content (modules).
 */
@Data
public class CourseContentRequest {

    @NotBlank(message = "Content title is required")
    private String title;

    private String description;

    private String contentUrl;

    private String contentType;

    @NotNull(message = "Sequence order is required")
    @Positive(message = "Sequence order must be positive")
    private Integer sequenceOrder;

    private Integer durationMinutes;
}
