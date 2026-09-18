package com.mythbreak.controller;

import com.mythbreak.dto.CourseContentResponse;
import com.mythbreak.dto.CourseResponse;
import com.mythbreak.entity.User;
import com.mythbreak.service.CourseService;
import com.mythbreak.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public course browsing endpoints.
 * Enrollment endpoint requires LEARNER role.
 */
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses(
            @RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(courseService.searchCourses(search));
        }
        return ResponseEntity.ok(courseService.getAllPublishedCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/{id}/content")
    public ResponseEntity<List<CourseContentResponse>> getCourseContent(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseContents(id));
    }

    @PostMapping("/{id}/enroll")
    public ResponseEntity<?> enroll(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enroll(user.getId(), id));
    }
}
