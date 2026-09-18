package com.mythbreak.repository;

import com.mythbreak.entity.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseContentRepository extends JpaRepository<CourseContent, Long> {

    List<CourseContent> findByCourseIdOrderBySequenceOrderAsc(Long courseId);

    void deleteByCourseId(Long courseId);
}
