package com.mythbreak.dto;

import lombok.Data;

/**
 * Request body for updating learner profile.
 */
@Data
public class LearnerProfileRequest {
    private String bio;
    private String phoneNumber;
    private String city;
    private String country;
    private String profileImageUrl;
    private String linkedinUrl;
    private String githubUrl;
}
