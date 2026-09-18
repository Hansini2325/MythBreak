package com.mythbreak.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Company / recruiter profile — one-to-one with User of role COMPANY.
 */
@Entity
@Table(name = "company_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String companyName;

    @Column(length = 2000)
    private String description;

    private String industry;

    private String website;

    private String logoUrl;

    private String city;

    private String country;

    private String phoneNumber;

    private Integer companySize;
}
