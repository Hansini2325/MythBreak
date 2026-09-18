package com.mythbreak.dto;

import com.mythbreak.enums.OpportunityStatus;
import com.mythbreak.enums.OpportunityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO for an opportunity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpportunityResponse {

    private Long id;
    private String title;
    private String description;
    private OpportunityType type;
    private String location;
    private String salaryRange;
    private LocalDate deadline;
    private OpportunityStatus status;
    private LocalDateTime createdAt;

    // Company info
    private Long companyId;
    private String companyName;
    private String companyLogoUrl;

    private List<SkillResponse> requiredSkills;
    private long applicationCount;
}
