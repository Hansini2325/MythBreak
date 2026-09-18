package com.mythbreak.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Educator profile — one-to-one with User of role EDUCATOR.
 */
@Entity
@Table(name = "educator_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducatorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 2000)
    private String bio;

    private String phoneNumber;

    private String city;

    private String country;

    private String profileImageUrl;

    private String linkedinUrl;

    private String websiteUrl;

    /** Comma-separated list of expertise areas e.g. "Java, Machine Learning" */
    @Column(length = 1000)
    private String expertise;

    private String qualification;

    private Integer yearsOfExperience;
}
