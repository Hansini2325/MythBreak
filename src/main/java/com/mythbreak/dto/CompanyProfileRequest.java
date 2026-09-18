package com.mythbreak.dto;

import lombok.Data;

/**
 * Request body for updating company profile.
 */
@Data
public class CompanyProfileRequest {
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
