package com.mythbreak.service;

import com.mythbreak.dto.MatchResult;
import com.mythbreak.entity.Application;
import com.mythbreak.entity.EarnerProfile;
import com.mythbreak.entity.Opportunity;
import com.mythbreak.entity.Skill;
import com.mythbreak.exception.ResourceNotFoundException;
import com.mythbreak.repository.ApplicationRepository;
import com.mythbreak.repository.EarnerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SKILL MATCHING ALGORITHM
 *
 * Compares an earner's skills against an opportunity's required skills.
 * Formula: matchPercentage = (matchedSkillCount / totalRequiredSkills) × 100
 *
 * Edge cases handled:
 * - No required skills → 100% for everyone
 * - No earner skills → 0%
 * - Case-insensitive comparison
 * - Duplicate skills (de-duplicated via Sets)
 * - Null values
 *
 * Returns candidates ranked by match percentage (highest first).
 */
@Service
@RequiredArgsConstructor
public class MatchingService {

    private final EarnerProfileRepository earnerProfileRepository;
    private final ApplicationRepository applicationRepository;
    private final OpportunityService opportunityService;

    /**
     * Calculate match for a single earner against an opportunity.
     */
    public MatchResult calculateMatch(EarnerProfile earner, Opportunity opportunity) {
        Set<String> requiredSkillNames = normalizeSkillNames(opportunity.getRequiredSkills());
        Set<String> earnerSkillNames = normalizeSkillNames(earner.getSkills());

        if (requiredSkillNames.isEmpty()) {
            return MatchResult.builder()
                    .earnerId(earner.getId())
                    .earnerName(earner.getUser().getFirstName() + " " + earner.getUser().getLastName())
                    .earnerEmail(earner.getUser().getEmail())
                    .matchPercentage(100.0)
                    .matchedSkills(new ArrayList<>(earnerSkillNames))
                    .missingSkills(Collections.emptyList())
                    .totalRequiredSkills(0)
                    .matchedCount(0)
                    .build();
        }

        List<String> matchedSkills = requiredSkillNames.stream()
                .filter(earnerSkillNames::contains)
                .sorted()
                .collect(Collectors.toList());

        List<String> missingSkills = requiredSkillNames.stream()
                .filter(s -> !earnerSkillNames.contains(s))
                .sorted()
                .collect(Collectors.toList());

        int totalRequired = requiredSkillNames.size();
        int matchedCount = matchedSkills.size();
        double percentage = ((double) matchedCount / totalRequired) * 100.0;

        return MatchResult.builder()
                .earnerId(earner.getId())
                .earnerName(earner.getUser().getFirstName() + " " + earner.getUser().getLastName())
                .earnerEmail(earner.getUser().getEmail())
                .matchPercentage(Math.round(percentage * 10.0) / 10.0)
                .matchedSkills(matchedSkills)
                .missingSkills(missingSkills)
                .totalRequiredSkills(totalRequired)
                .matchedCount(matchedCount)
                .build();
    }

    /**
     * Calculate match for the currently authenticated earner against an opportunity.
     */
    public MatchResult getMyMatch(Long earnerUserId, Long opportunityId) {
        EarnerProfile earner = earnerProfileRepository.findByUserId(earnerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("EarnerProfile", "userId", earnerUserId));
        Opportunity opportunity = opportunityService.findOrThrow(opportunityId);
        return calculateMatch(earner, opportunity);
    }

    /**
     * Rank all applicants for an opportunity by match percentage.
     * Returns list sorted by match percentage descending.
     */
    public List<MatchResult> rankCandidates(Long opportunityId) {
        Opportunity opportunity = opportunityService.findOrThrow(opportunityId);

        List<Application> applications = applicationRepository.findByOpportunityId(opportunityId);

        return applications.stream()
                .map(app -> calculateMatch(app.getEarner(), opportunity))
                .sorted(Comparator.comparingDouble(MatchResult::getMatchPercentage).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Rank ALL earners on the platform against an opportunity.
     * Used by companies to discover un-applied candidates too.
     */
    public List<MatchResult> rankAllEarners(Long opportunityId) {
        Opportunity opportunity = opportunityService.findOrThrow(opportunityId);
        List<EarnerProfile> allEarners = earnerProfileRepository.findAll();

        return allEarners.stream()
                .map(earner -> calculateMatch(earner, opportunity))
                .filter(result -> result.getMatchPercentage() > 0)
                .sorted(Comparator.comparingDouble(MatchResult::getMatchPercentage).reversed())
                .collect(Collectors.toList());
    }

    // ---- Private helper ----

    /**
     * Convert a set of Skill entities to a set of normalized lowercase names.
     * This ensures case-insensitive comparison (Java vs java vs JAVA → all equal).
     */
    private Set<String> normalizeSkillNames(Set<Skill> skills) {
        if (skills == null || skills.isEmpty()) return Collections.emptySet();
        return skills.stream()
                .filter(Objects::nonNull)
                .map(Skill::getName)
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}
