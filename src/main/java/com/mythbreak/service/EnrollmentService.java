package com.mythbreak.service;

import com.mythbreak.dto.EnrollmentResponse;
import com.mythbreak.dto.ProgressUpdateRequest;
import com.mythbreak.entity.Course;
import com.mythbreak.entity.Enrollment;
import com.mythbreak.entity.LearnerProfile;
import com.mythbreak.enums.EnrollmentStatus;
import com.mythbreak.enums.NotificationType;
import com.mythbreak.exception.BadRequestException;
import com.mythbreak.exception.DuplicateResourceException;
import com.mythbreak.exception.ResourceNotFoundException;
import com.mythbreak.repository.CourseRepository;
import com.mythbreak.repository.EnrollmentRepository;
import com.mythbreak.repository.LearnerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for enrolling learners, tracking progress, and completing courses.
 */
@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final LearnerProfileRepository learnerProfileRepository;
    private final CourseRepository courseRepository;
    private final NotificationService notificationService;

    @Transactional
    public EnrollmentResponse enroll(Long userId, Long courseId) {
        LearnerProfile learner = learnerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("LearnerProfile", "userId", userId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        if (!course.isPublished()) {
            throw new BadRequestException("This course is not yet published.");
        }

        if (enrollmentRepository.existsByLearnerIdAndCourseId(learner.getId(), courseId)) {
            throw new DuplicateResourceException("You are already enrolled in this course.");
        }

        Enrollment enrollment = Enrollment.builder()
                .learner(learner)
                .course(course)
                .status(EnrollmentStatus.ACTIVE)
                .progressPercent(0)
                .build();

        Enrollment saved = enrollmentRepository.save(enrollment);

        notificationService.createNotification(
                learner.getUser(),
                "Enrollment Successful",
                "You have enrolled in: " + course.getTitle(),
                NotificationType.COURSE_ENROLLED
        );

        return toResponse(saved);
    }

    @Transactional
    public EnrollmentResponse updateProgress(Long userId, Long courseId,
                                              ProgressUpdateRequest request) {
        LearnerProfile learner = learnerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("LearnerProfile", "userId", userId));

        Enrollment enrollment = enrollmentRepository
                .findByLearnerIdAndCourseId(learner.getId(), courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enrollment not found for this course."));

        enrollment.setProgressPercent(request.getProgressPercent());

        if (request.getProgressPercent() == 100) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
            enrollment.setCompletedAt(LocalDateTime.now());
            notificationService.createNotification(
                    learner.getUser(),
                    "Course Completed!",
                    "Congratulations! You completed: " + enrollment.getCourse().getTitle(),
                    NotificationType.COURSE_COMPLETED
            );
        }

        return toResponse(enrollmentRepository.save(enrollment));
    }

    public List<EnrollmentResponse> getLearnerEnrollments(Long userId) {
        LearnerProfile learner = learnerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("LearnerProfile", "userId", userId));

        return enrollmentRepository.findByLearnerId(learner.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<EnrollmentResponse> getEnrollmentsByCourse(Long courseId, Long educatorUserId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::toResponse)
                .toList();
    }

    public EnrollmentResponse toResponse(Enrollment e) {
        return EnrollmentResponse.builder()
                .id(e.getId())
                .learnerId(e.getLearner().getId())
                .learnerName(e.getLearner().getUser().getFirstName() + " " +
                             e.getLearner().getUser().getLastName())
                .courseId(e.getCourse().getId())
                .courseName(e.getCourse().getTitle())
                .enrolledAt(e.getEnrolledAt())
                .progressPercent(e.getProgressPercent())
                .status(e.getStatus())
                .completedAt(e.getCompletedAt())
                .build();
    }
}
