package com.mythbreak.dto;

import com.mythbreak.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for an application.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {

    private Long id;

    // Earner info
    private Long earnerId;
    private String earnerName;
    private String earnerEmail;

    // Opportunity info
    private Long opportunityId;
    private String opportunityTitle;
    private String companyName;

    private LocalDateTime appliedAt;
    private ApplicationStatus status;
    private String coverLetter;
}
