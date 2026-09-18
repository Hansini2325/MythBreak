package com.mythbreak.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request body for updating enrollment progress.
 */
@Data
public class ProgressUpdateRequest {

    @NotNull(message = "Progress percent is required")
    @Min(value = 0, message = "Progress cannot be less than 0")
    @Max(value = 100, message = "Progress cannot exceed 100")
    private Integer progressPercent;
}
