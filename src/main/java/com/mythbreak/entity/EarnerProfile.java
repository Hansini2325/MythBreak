package com.mythbreak.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Earner profile — one-to-one with User of role EARNER.
 * Contains professional details and a set of skills.
 */
@Entity
@Table(name = "earner_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EarnerProfile {

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

    private String githubUrl;

    private String portfolioUrl;

    private String resumeUrl;

    private Integer yearsOfExperience;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "earner_skills",
            joinColumns = @JoinColumn(name = "earner_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @Builder.Default
    private Set<Skill> skills = new HashSet<>();
}
