package com.mythbreak.repository;

import com.mythbreak.entity.EarnerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EarnerProfileRepository extends JpaRepository<EarnerProfile, Long> {

    Optional<EarnerProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
