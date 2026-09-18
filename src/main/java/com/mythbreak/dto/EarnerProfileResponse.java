package com.mythbreak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for earner profile.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EarnerProfileResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String bio;
    private String phoneNumber;
    private String city;
    private String country;
    private String profileImageUrl;
    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String resumeUrl;
    private Integer yearsOfExperience;
    private List<SkillResponse> skills;
}
