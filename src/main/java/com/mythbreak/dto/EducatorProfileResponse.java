package com.mythbreak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for educator profile.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducatorProfileResponse {
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
    private String websiteUrl;
    private String expertise;
    private String qualification;
    private Integer yearsOfExperience;
}
