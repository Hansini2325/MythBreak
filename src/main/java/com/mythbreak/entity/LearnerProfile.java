package com.mythbreak.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Learner profile — one-to-one with User of role LEARNER.
 */
@Entity
@Table(name = "learner_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearnerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 1000)
    private String bio;

    private String phoneNumber;

    private String city;

    private String country;

    private String profileImageUrl;

    private String linkedinUrl;

    private String githubUrl;
}
