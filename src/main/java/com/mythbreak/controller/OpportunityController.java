package com.mythbreak.controller;

import com.mythbreak.dto.ApplicationRequest;
import com.mythbreak.dto.ApplicationResponse;
import com.mythbreak.dto.MatchResult;
import com.mythbreak.dto.OpportunityResponse;
import com.mythbreak.entity.User;
import com.mythbreak.service.ApplicationService;
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
 * Public opportunity browsing; Earner application endpoints.
 */
@RestController
@RequestMapping("/api/opportunities")
@RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityService opportunityService;
    private final ApplicationService applicationService;
    private final MatchingService matchingService;

    @GetMapping
    public ResponseEntity<List<OpportunityResponse>> getAllOpportunities(
            @RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(opportunityService.searchOpportunities(search));
        }
        return ResponseEntity.ok(opportunityService.getAllOpenOpportunities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OpportunityResponse> getOpportunityById(@PathVariable Long id) {
        return ResponseEntity.ok(opportunityService.getOpportunityById(id));
    }

    /**
     * POST /api/opportunities/{id}/apply
     * Earner applies to an opportunity.
     */
    @PostMapping("/{id}/apply")
    public ResponseEntity<ApplicationResponse> apply(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody ApplicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationService.apply(user.getId(), id, request));
    }

    /**
     * GET /api/opportunities/{id}/my-match
     * Earner checks their own match percentage for an opportunity.
     */
    @GetMapping("/{id}/my-match")
    public ResponseEntity<MatchResult> getMyMatch(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        return ResponseEntity.ok(matchingService.getMyMatch(user.getId(), id));
    }

    /**
     * GET /api/opportunities/{id}/matches
     * Company/Admin views ranked candidates by skill match.
     */
    @GetMapping("/{id}/matches")
    public ResponseEntity<List<MatchResult>> getMatches(@PathVariable Long id) {
        return ResponseEntity.ok(matchingService.rankAllEarners(id));
    }
}
