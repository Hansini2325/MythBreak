package com.mythbreak.controller;

import com.mythbreak.dto.EnrollmentResponse;
import com.mythbreak.dto.LearnerProfileRequest;
import com.mythbreak.dto.LearnerProfileResponse;
import com.mythbreak.dto.NotificationResponse;
import com.mythbreak.dto.ProgressUpdateRequest;
import com.mythbreak.entity.User;
import com.mythbreak.service.EnrollmentService;
import com.mythbreak.service.LearnerService;
import com.mythbreak.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Learner-specific endpoints.
 * All require LEARNER role.
 */
@RestController
@RequestMapping("/api/learner")
@RequiredArgsConstructor
public class LearnerController {

    private final LearnerService learnerService;
    private final EnrollmentService enrollmentService;
    private final NotificationService notificationService;

    @GetMapping("/profile")
    public ResponseEntity<LearnerProfileResponse> getProfile(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(learnerService.getProfile(user.getId()));
    }

    @PutMapping("/profile")
    public ResponseEntity<LearnerProfileResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody LearnerProfileRequest request) {
        return ResponseEntity.ok(learnerService.createOrUpdateProfile(user.getId(), request));
    }

    @GetMapping("/enrollments")
    public ResponseEntity<List<EnrollmentResponse>> getMyEnrollments(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(enrollmentService.getLearnerEnrollments(user.getId()));
    }

    @PostMapping("/enrollments/{courseId}")
    public ResponseEntity<EnrollmentResponse> enroll(
            @AuthenticationPrincipal User user,
            @PathVariable Long courseId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enroll(user.getId(), courseId));
    }

    @PutMapping("/enrollments/{courseId}/progress")
    public ResponseEntity<EnrollmentResponse> updateProgress(
            @AuthenticationPrincipal User user,
            @PathVariable Long courseId,
            @Valid @RequestBody ProgressUpdateRequest request) {
        return ResponseEntity.ok(enrollmentService.updateProgress(user.getId(), courseId, request));
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(notificationService.getUserNotifications(user.getId()));
    }
}
