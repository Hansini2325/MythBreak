package com.mythbreak.controller;

import com.mythbreak.dto.*;
import com.mythbreak.entity.User;
import com.mythbreak.enums.ApplicationStatus;
import com.mythbreak.service.ApplicationService;
import com.mythbreak.service.CompanyService;
import com.mythbreak.service.MatchingService;
import com.mythbreak.service.OpportunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Company-specific endpoints: profile, opportunities, candidates, applications.
 * All require COMPANY role.
 */
@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final OpportunityService opportunityService;
    private final ApplicationService applicationService;
    private final MatchingService matchingService;

    // ---- Profile ----

    @GetMapping("/profile")
    public ResponseEntity<CompanyProfileResponse> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(companyService.getProfile(user.getId()));
    }

    @PutMapping("/profile")
    public ResponseEntity<CompanyProfileResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody CompanyProfileRequest request) {
        return ResponseEntity.ok(companyService.createOrUpdateProfile(user.getId(), request));
    }

    // ---- Opportunities ----

    @GetMapping("/opportunities")
    public ResponseEntity<List<OpportunityResponse>> getMyOpportunities(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(opportunityService.getOpportunitiesByCompany(user.getId()));
    }

    @PostMapping("/opportunities")
    public ResponseEntity<OpportunityResponse> createOpportunity(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody OpportunityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(opportunityService.createOpportunity(user.getId(), request));
    }

    @PutMapping("/opportunities/{id}")
    public ResponseEntity<OpportunityResponse> updateOpportunity(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody OpportunityRequest request) {
        return ResponseEntity.ok(opportunityService.updateOpportunity(user.getId(), id, request));
    }

    @PatchMapping("/opportunities/{id}/close")
    public ResponseEntity<OpportunityResponse> closeOpportunity(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        return ResponseEntity.ok(opportunityService.closeOpportunity(user.getId(), id));
    }

    @DeleteMapping("/opportunities/{id}")
    public ResponseEntity<Void> deleteOpportunity(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        opportunityService.deleteOpportunity(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

    // ---- Applications management ----

    /** GET /api/company/applications — all applications across all company opportunities */
    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationResponse>> getAllApplications(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(applicationService.getApplicationsByCompany(user.getId()));
    }

    @GetMapping("/opportunities/{opportunityId}/applications")
    public ResponseEntity<List<ApplicationResponse>> getApplications(
            @PathVariable Long opportunityId) {
        return ResponseEntity.ok(applicationService.getApplicationsForOpportunity(opportunityId));
    }

    /** PATCH /api/company/applications/{applicationId}/status — shortcut endpoint */
    @PatchMapping("/applications/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateApplicationStatusShortcut(
            @AuthenticationPrincipal User user,
            @PathVariable Long applicationId,
            @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(
                applicationService.updateStatusByAppId(applicationId, status, user.getId()));
    }

    @PatchMapping("/opportunities/{opportunityId}/applications/{applicationId}/status")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Long opportunityId,
            @PathVariable Long applicationId,
            @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(
                applicationService.updateStatus(opportunityId, applicationId, status, user.getId()));
    }

    // ---- Candidate matching ----

    @GetMapping("/opportunities/{opportunityId}/matches")
    public ResponseEntity<List<MatchResult>> getCandidateMatches(
            @PathVariable Long opportunityId) {
        return ResponseEntity.ok(matchingService.rankAllEarners(opportunityId));
    }

    @GetMapping("/opportunities/{opportunityId}/applicant-matches")
    public ResponseEntity<List<MatchResult>> getApplicantMatches(
            @PathVariable Long opportunityId) {
        return ResponseEntity.ok(matchingService.rankCandidates(opportunityId));
    }
}
