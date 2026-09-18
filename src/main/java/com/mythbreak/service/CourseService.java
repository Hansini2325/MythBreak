package com.mythbreak.service;

import com.mythbreak.dto.CourseContentRequest;
import com.mythbreak.dto.CourseContentResponse;
import com.mythbreak.dto.CourseRequest;
import com.mythbreak.dto.CourseResponse;
import com.mythbreak.entity.Course;
import com.mythbreak.entity.CourseContent;
import com.mythbreak.entity.EducatorProfile;
import com.mythbreak.exception.ResourceNotFoundException;
import com.mythbreak.exception.UnauthorizedException;
import com.mythbreak.repository.CourseContentRepository;
import com.mythbreak.repository.CourseRepository;
import com.mythbreak.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for course CRUD and content management.
 */
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseContentRepository courseContentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EducatorService educatorService;

    // ---- Course operations ----

    public List<CourseResponse> getAllPublishedCourses() {
        return courseRepository.findByPublishedTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CourseResponse> searchCourses(String keyword) {
        return courseRepository.searchPublishedCourses(keyword).stream()
                .map(this::toResponse)
                .toList();
    }

    public CourseResponse getCourseById(Long courseId) {
        Course course = findCourseOrThrow(courseId);
        return toResponse(course);
    }

    public List<CourseResponse> getCoursesByEducator(Long userId) {
        EducatorProfile educator = educatorService.getEducatorEntityByUserId(userId);
        return courseRepository.findByEducatorId(educator.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CourseResponse createCourse(Long userId, CourseRequest request) {
        EducatorProfile educator = educatorService.getEducatorEntityByUserId(userId);

        Course course = Course.builder()
                .educator(educator)
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .level(request.getLevel())
                .category(request.getCategory())
                .thumbnailUrl(request.getThumbnailUrl())
                .published(request.isPublished())
                .build();

        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse updateCourse(Long userId, Long courseId, CourseRequest request) {
        Course course = findCourseOrThrow(courseId);
        verifyOwnership(userId, course);

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setLevel(request.getLevel());
        course.setCategory(request.getCategory());
        course.setThumbnailUrl(request.getThumbnailUrl());
        course.setPublished(request.isPublished());

        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public void deleteCourse(Long userId, Long courseId) {
        Course course = findCourseOrThrow(courseId);
        verifyOwnership(userId, course);
        courseRepository.delete(course);
    }

    // ---- Course content operations ----

    public List<CourseContentResponse> getCourseContents(Long courseId) {
        findCourseOrThrow(courseId);
        return courseContentRepository.findByCourseIdOrderBySequenceOrderAsc(courseId).stream()
                .map(this::toContentResponse)
                .toList();
    }

    @Transactional
    public CourseContentResponse addCourseContent(Long userId, Long courseId,
                                                   CourseContentRequest request) {
        Course course = findCourseOrThrow(courseId);
        verifyOwnership(userId, course);

        CourseContent content = CourseContent.builder()
                .course(course)
                .title(request.getTitle())
                .description(request.getDescription())
                .contentUrl(request.getContentUrl())
                .contentType(request.getContentType())
                .sequenceOrder(request.getSequenceOrder())
                .durationMinutes(request.getDurationMinutes())
                .build();

        return toContentResponse(courseContentRepository.save(content));
    }

    @Transactional
    public CourseContentResponse updateCourseContent(Long userId, Long courseId, Long contentId,
                                                      CourseContentRequest request) {
        Course course = findCourseOrThrow(courseId);
        verifyOwnership(userId, course);

        CourseContent content = courseContentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("CourseContent", "id", contentId));

        content.setTitle(request.getTitle());
        content.setDescription(request.getDescription());
        content.setContentUrl(request.getContentUrl());
        content.setContentType(request.getContentType());
        content.setSequenceOrder(request.getSequenceOrder());
        content.setDurationMinutes(request.getDurationMinutes());

        return toContentResponse(courseContentRepository.save(content));
    }

    @Transactional
    public void deleteCourseContent(Long userId, Long courseId, Long contentId) {
        Course course = findCourseOrThrow(courseId);
        verifyOwnership(userId, course);
        courseContentRepository.deleteById(contentId);
    }

    // ---- Helper methods ----

    private Course findCourseOrThrow(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
    }

    private void verifyOwnership(Long userId, Course course) {
        EducatorProfile educator = educatorService.getEducatorEntityByUserId(userId);
        if (!course.getEducator().getId().equals(educator.getId())) {
            throw new UnauthorizedException("You are not the owner of this course.");
        }
    }

    public CourseResponse toResponse(Course course) {
        long enrolled = enrollmentRepository.countActiveEnrollmentsByCourseId(course.getId());

        List<CourseContentResponse> contentResponses = course.getContents().stream()
                .map(this::toContentResponse)
                .toList();

        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .price(course.getPrice())
                .level(course.getLevel())
                .category(course.getCategory())
                .thumbnailUrl(course.getThumbnailUrl())
                .published(course.isPublished())
                .createdAt(course.getCreatedAt())
                .educatorId(course.getEducator().getId())
                .educatorName(course.getEducator().getUser().getFirstName() + " " +
                              course.getEducator().getUser().getLastName())
                .contents(contentResponses)
                .enrolledCount(enrolled)
                .build();
    }

    public CourseContentResponse toContentResponse(CourseContent content) {
        return CourseContentResponse.builder()
                .id(content.getId())
                .title(content.getTitle())
                .description(content.getDescription())
                .contentUrl(content.getContentUrl())
                .contentType(content.getContentType())
                .sequenceOrder(content.getSequenceOrder())
                .durationMinutes(content.getDurationMinutes())
                .courseId(content.getCourse().getId())
                .build();
    }
}
