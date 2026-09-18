package com.mythbreak.repository;

import com.mythbreak.entity.Enrollment;
import com.mythbreak.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByLearnerIdAndCourseId(Long learnerProfileId, Long courseId);

    boolean existsByLearnerIdAndCourseId(Long learnerProfileId, Long courseId);

    List<Enrollment> findByLearnerId(Long learnerProfileId);

    List<Enrollment> findByCourseId(Long courseId);

    List<Enrollment> findByLearnerIdAndStatus(Long learnerProfileId, EnrollmentStatus status);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.status = 'ACTIVE'")
    long countActiveEnrollmentsByCourseId(@Param("courseId") Long courseId);
}
