package com.mythbreak.service;

import com.mythbreak.dto.SkillRequest;
import com.mythbreak.dto.SkillResponse;
import com.mythbreak.entity.Skill;
import com.mythbreak.exception.DuplicateResourceException;
import com.mythbreak.exception.ResourceNotFoundException;
import com.mythbreak.repository.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SkillService Unit Tests")
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;

    private Skill javaSkill;

    @BeforeEach
    void setUp() {
        javaSkill = Skill.builder().id(1L).name("Java").category("Programming").build();
    }

    @Test
    @DisplayName("Get all skills returns list")
    void getAllSkills_ReturnsAllSkills() {
        when(skillRepository.findAll()).thenReturn(List.of(javaSkill));

        List<SkillResponse> result = skillService.getAllSkills();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Java");
    }

    @Test
    @DisplayName("Create skill - successful")
    void createSkill_NewSkill_ReturnsCreatedSkill() {
        SkillRequest request = new SkillRequest();
        request.setName("Python");
        request.setCategory("Programming");

        Skill saved = Skill.builder().id(2L).name("Python").category("Programming").build();

        when(skillRepository.existsByNameIgnoreCaseAndCategoryIgnoreCase("Python", "Programming"))
                .thenReturn(false);
        when(skillRepository.save(any(Skill.class))).thenReturn(saved);

        SkillResponse response = skillService.createSkill(request);

        assertThat(response.getName()).isEqualTo("Python");
        assertThat(response.getCategory()).isEqualTo("Programming");
    }

    @Test
    @DisplayName("Create skill - duplicate throws DuplicateResourceException")
    void createSkill_Duplicate_ThrowsException() {
        SkillRequest request = new SkillRequest();
        request.setName("Java");
        request.setCategory("Programming");

        when(skillRepository.existsByNameIgnoreCaseAndCategoryIgnoreCase("Java", "Programming"))
                .thenReturn(true);

        assertThatThrownBy(() -> skillService.createSkill(request))
                .isInstanceOf(DuplicateResourceException.class);

        verify(skillRepository, never()).save(any());
    }

    @Test
    @DisplayName("Get skill by ID - not found throws ResourceNotFoundException")
    void getSkillById_NotFound_ThrowsException() {
        when(skillRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> skillService.getSkillById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
