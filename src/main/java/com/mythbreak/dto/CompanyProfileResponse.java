package com.mythbreak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for company profile.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyProfileResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String companyName;
    private String description;
    private String industry;
    private String website;
    private String logoUrl;
    private String city;
    private String country;
    private String phoneNumber;
    private Integer companySize;
}
