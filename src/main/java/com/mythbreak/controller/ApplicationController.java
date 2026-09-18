package com.mythbreak.controller;

import com.mythbreak.dto.ApplicationResponse;
import com.mythbreak.dto.NotificationResponse;
import com.mythbreak.entity.User;
import com.mythbreak.service.ApplicationService;
import com.mythbreak.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Application tracking and notification endpoints for earners.
 */
@RestController
@RequestMapping("/api/earner")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final NotificationService notificationService;

    // ---- Applications ----

    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(applicationService.getEarnerApplications(user.getId()));
    }

    // ---- Notifications ----

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(notificationService.getUserNotifications(user.getId()));
    }

    @GetMapping("/notifications/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of("count", notificationService.getUnreadCount(user.getId())));
    }

    @PatchMapping("/notifications/mark-all-read")
    public ResponseEntity<Void> markAllRead(@AuthenticationPrincipal User user) {
        notificationService.markAllAsRead(user.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/notifications/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}
