package com.mythbreak.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Request body for creating / updating a course.
 */
@Data
public class CourseRequest {

    @NotBlank(message = "Course title is required")
    @Size(max = 200)
    private String title;

    @Size(max = 3000)
    private String description;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @NotBlank(message = "Course level is required")
    private String level;

    @NotBlank(message = "Course category is required")
    private String category;

    private String thumbnailUrl;

    private boolean published = false;
}
