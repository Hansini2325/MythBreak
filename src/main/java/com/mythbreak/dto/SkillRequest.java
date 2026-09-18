package com.mythbreak.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request body for creating / updating a skill.
 */
@Data
public class SkillRequest {

    @NotBlank(message = "Skill name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Skill category is required")
    @Size(max = 100)
    private String category;
}
