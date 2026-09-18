package com.mythbreak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Response DTO for a portfolio project.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private Long id;
    private Long earnerId;
    private String earnerName;
    private String title;
    private String description;
    private String projectUrl;
    private String githubUrl;
    private String demoUrl;
    private LocalDate completionDate;
    private List<SkillResponse> technologies;
}
