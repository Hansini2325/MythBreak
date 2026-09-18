package com.mythbreak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Result of the skill-matching algorithm for a single candidate against an opportunity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {

    private Long earnerId;
    private String earnerName;
    private String earnerEmail;

    /** Percentage of required skills matched (0.0 – 100.0) */
    private double matchPercentage;

    private List<String> matchedSkills;

    private List<String> missingSkills;

    private int totalRequiredSkills;

    private int matchedCount;
}
