package com.mythbreak.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Request body for creating / updating a portfolio project.
 */
@Data
public class ProjectRequest {

    @NotBlank(message = "Project title is required")
    @Size(max = 200)
    private String title;

    @Size(max = 3000)
    private String description;

    private String projectUrl;

    private String githubUrl;

    private String demoUrl;

    private LocalDate completionDate;

    /** List of skill IDs to associate with this project */
    private List<Long> skillIds;
}
