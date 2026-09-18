package com.mythbreak.controller;

import com.mythbreak.dto.*;
import com.mythbreak.entity.User;
import com.mythbreak.service.EarnerService;
import com.mythbreak.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Earner-specific endpoints: profile, skills, projects.
 * All require EARNER role.
 */
@RestController
@RequestMapping("/api/earner")
@RequiredArgsConstructor
public class EarnerController {

    private final EarnerService earnerService;
    private final ProjectService projectService;

    // ---- Profile ----

    @GetMapping("/profile")
    public ResponseEntity<EarnerProfileResponse> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(earnerService.getProfile(user.getId()));
    }

    @PutMapping("/profile")
    public ResponseEntity<EarnerProfileResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody EarnerProfileRequest request) {
        return ResponseEntity.ok(earnerService.createOrUpdateProfile(user.getId(), request));
    }

    // ---- Skills ----

    @GetMapping("/skills")
    public ResponseEntity<List<SkillResponse>> getMySkills(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(earnerService.getEarnerSkills(user.getId()));
    }

    @PostMapping("/skills/{skillId}")
    public ResponseEntity<EarnerProfileResponse> addSkill(
            @AuthenticationPrincipal User user,
            @PathVariable Long skillId) {
        return ResponseEntity.ok(earnerService.addSkill(user.getId(), skillId));
    }

    @DeleteMapping("/skills/{skillId}")
    public ResponseEntity<EarnerProfileResponse> removeSkill(
            @AuthenticationPrincipal User user,
            @PathVariable Long skillId) {
        return ResponseEntity.ok(earnerService.removeSkill(user.getId(), skillId));
    }

    // ---- Projects (Portfolio) ----

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectResponse>> getMyProjects(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(projectService.getProjectsByEarner(user.getId()));
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectResponse> createProject(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(user.getId(), request));
    }

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(
            @AuthenticationPrincipal User user,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.updateProject(user.getId(), projectId, request));
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @AuthenticationPrincipal User user,
            @PathVariable Long projectId) {
        projectService.deleteProject(user.getId(), projectId);
        return ResponseEntity.noContent().build();
    }
}
