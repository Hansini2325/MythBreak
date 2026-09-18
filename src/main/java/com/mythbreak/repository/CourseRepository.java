package com.mythbreak.repository;

import com.mythbreak.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByPublishedTrue();

    List<Course> findByEducatorId(Long educatorProfileId);

    List<Course> findByEducatorIdAndPublishedTrue(Long educatorProfileId);

    @Query("SELECT c FROM Course c WHERE c.published = true AND " +
           "(LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(c.category) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Course> searchPublishedCourses(@Param("keyword") String keyword);

    List<Course> findByCategoryIgnoreCaseAndPublishedTrue(String category);
}
