package com.mythbreak.controller;

import com.mythbreak.dto.*;
import com.mythbreak.entity.User;
import com.mythbreak.service.CourseService;
import com.mythbreak.service.EducatorService;
import com.mythbreak.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Educator-specific endpoints.
 * All require EDUCATOR role.
 */
@RestController
@RequestMapping("/api/educator")
@RequiredArgsConstructor
public class EducatorController {

    private final EducatorService educatorService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    // ---- Profile ----

    @GetMapping("/profile")
    public ResponseEntity<EducatorProfileResponse> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(educatorService.getProfile(user.getId()));
    }

    @PutMapping("/profile")
    public ResponseEntity<EducatorProfileResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody EducatorProfileRequest request) {
        return ResponseEntity.ok(educatorService.createOrUpdateProfile(user.getId(), request));
    }

    // ---- Courses ----

    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getMyCourses(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(courseService.getCoursesByEducator(user.getId()));
    }

    @PostMapping("/courses")
    public ResponseEntity<CourseResponse> createCourse(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.createCourse(user.getId(), request));
    }

    @PutMapping("/courses/{courseId}")
    public ResponseEntity<CourseResponse> updateCourse(
            @AuthenticationPrincipal User user,
            @PathVariable Long courseId,
            @Valid @RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.updateCourse(user.getId(), courseId, request));
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCourse(
            @AuthenticationPrincipal User user,
            @PathVariable Long courseId) {
        courseService.deleteCourse(user.getId(), courseId);
        return ResponseEntity.noContent().build();
    }

    // ---- Course Content ----

    @PostMapping("/courses/{courseId}/content")
    public ResponseEntity<CourseContentResponse> addContent(
            @AuthenticationPrincipal User user,
            @PathVariable Long courseId,
            @Valid @RequestBody CourseContentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.addCourseContent(user.getId(), courseId, request));
    }

    @PutMapping("/courses/{courseId}/content/{contentId}")
    public ResponseEntity<CourseContentResponse> updateContent(
            @AuthenticationPrincipal User user,
            @PathVariable Long courseId,
            @PathVariable Long contentId,
            @Valid @RequestBody CourseContentRequest request) {
        return ResponseEntity.ok(
                courseService.updateCourseContent(user.getId(), courseId, contentId, request));
    }

    @DeleteMapping("/courses/{courseId}/content/{contentId}")
    public ResponseEntity<Void> deleteContent(
            @AuthenticationPrincipal User user,
            @PathVariable Long courseId,
            @PathVariable Long contentId) {
        courseService.deleteCourseContent(user.getId(), courseId, contentId);
        return ResponseEntity.noContent().build();
    }

    // ---- Enrolled Students ----

    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<List<EnrollmentResponse>> getEnrolledStudents(
            @AuthenticationPrincipal User user,
            @PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId, user.getId()));
    }
}
