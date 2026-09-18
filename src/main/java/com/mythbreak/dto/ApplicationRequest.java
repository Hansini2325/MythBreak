package com.mythbreak.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request body for submitting an application.
 */
@Data
public class ApplicationRequest {

    @Size(max = 2000, message = "Cover letter cannot exceed 2000 characters")
    private String coverLetter;
}
