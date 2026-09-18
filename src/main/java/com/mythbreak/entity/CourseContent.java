package com.mythbreak.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Individual content module inside a course (video, article, quiz, etc.).
 */
@Entity
@Table(name = "course_contents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String title;

    @Column(length = 5000)
    private String description;

    /** URL to the content (video, PDF, external link, etc.) */
    private String contentUrl;

    /** Type: VIDEO, ARTICLE, QUIZ, ASSIGNMENT */
    private String contentType;

    /** Order of the module within the course */
    @Column(nullable = false)
    private Integer sequenceOrder;

    /** Duration in minutes */
    private Integer durationMinutes;
}
