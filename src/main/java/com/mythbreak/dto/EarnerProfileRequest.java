package com.mythbreak.dto;

import lombok.Data;

import java.util.List;

/**
 * Request body for updating earner profile.
 */
@Data
public class EarnerProfileRequest {
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
}
