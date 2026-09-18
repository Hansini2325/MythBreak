package com.mythbreak.dto;

import com.mythbreak.enums.OpportunityType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * Request body for creating / updating an opportunity.
 */
@Data
public class OpportunityRequest {

    @NotBlank(message = "Opportunity title is required")
    @Size(max = 200)
    private String title;

    @Size(max = 5000)
    private String description;

    @NotNull(message = "Opportunity type is required")
    private OpportunityType type;

    private String location;

    private String salaryRange;

    @Future(message = "Deadline must be a future date")
    private LocalDate deadline;

    /** List of skill IDs required for this opportunity */
    private List<Long> requiredSkillIds;
}
