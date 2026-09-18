package com.mythbreak.dto;

import lombok.Data;

/**
 * Request body for updating educator profile.
 */
@Data
public class EducatorProfileRequest {
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
