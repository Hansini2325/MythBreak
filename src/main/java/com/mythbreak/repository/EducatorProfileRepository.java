package com.mythbreak.repository;

import com.mythbreak.entity.EducatorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EducatorProfileRepository extends JpaRepository<EducatorProfile, Long> {

    Optional<EducatorProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
