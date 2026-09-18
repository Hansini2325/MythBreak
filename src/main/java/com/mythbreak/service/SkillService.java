package com.mythbreak.service;

import com.mythbreak.dto.SkillRequest;
import com.mythbreak.dto.SkillResponse;
import com.mythbreak.entity.Skill;
import com.mythbreak.exception.DuplicateResourceException;
import com.mythbreak.exception.ResourceNotFoundException;
import com.mythbreak.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing the skills master list.
 */
@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public List<SkillResponse> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SkillResponse> getSkillsByCategory(String category) {
        return skillRepository.findByCategoryIgnoreCase(category).stream()
                .map(this::toResponse)
                .toList();
    }

    public SkillResponse getSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", id));
        return toResponse(skill);
    }

    @Transactional
    public SkillResponse createSkill(SkillRequest request) {
        String normalizedName = normalize(request.getName());
        String normalizedCategory = normalize(request.getCategory());

        if (skillRepository.existsByNameIgnoreCaseAndCategoryIgnoreCase(
                normalizedName, normalizedCategory)) {
            throw new DuplicateResourceException(
                    "Skill '" + request.getName() + "' already exists in category '" + request.getCategory() + "'");
        }

        Skill skill = Skill.builder()
                .name(normalizedName)
                .category(normalizedCategory)
                .build();

        return toResponse(skillRepository.save(skill));
    }

    @Transactional
    public void deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new ResourceNotFoundException("Skill", "id", id);
        }
        skillRepository.deleteById(id);
    }

    public SkillResponse toResponse(Skill skill) {
        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .category(skill.getCategory())
                .build();
    }

    /** Normalize: trim and capitalize first letter. Preserves case otherwise. */
    private String normalize(String value) {
        if (value == null || value.isBlank()) return value;
        String trimmed = value.trim();
        return Character.toUpperCase(trimmed.charAt(0)) + trimmed.substring(1);
    }
}
