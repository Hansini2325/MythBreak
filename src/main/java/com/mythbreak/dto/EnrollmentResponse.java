package com.mythbreak.dto;

import com.mythbreak.enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for an enrollment.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {

    private Long id;
    private Long learnerId;
    private String learnerName;
    private Long courseId;
    private String courseName;
    private LocalDateTime enrolledAt;
    private Integer progressPercent;
    private EnrollmentStatus status;
    private LocalDateTime completedAt;
}
