package com.mythbreak.repository;

import com.mythbreak.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findByNameIgnoreCaseAndCategoryIgnoreCase(String name, String category);

    List<Skill> findByNameIgnoreCase(String name);

    List<Skill> findByCategoryIgnoreCase(String category);

    boolean existsByNameIgnoreCaseAndCategoryIgnoreCase(String name, String category);
}
