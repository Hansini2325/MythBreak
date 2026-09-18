package com.mythbreak.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Skill entity — stored as master data, shared across users and opportunities.
 * Skills are case-normalized on save; matching is also case-insensitive.
 */
@Entity
@Table(name = "skills",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "category"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    // Bidirectional ManyToMany owners are Earner/Opportunity
    // We do NOT map back to avoid circular references in JSON
}
