package com.mythbreak.service;

import com.mythbreak.dto.MatchResult;
import com.mythbreak.entity.EarnerProfile;
import com.mythbreak.entity.Opportunity;
import com.mythbreak.entity.Skill;
import com.mythbreak.entity.User;
import com.mythbreak.enums.Role;
import com.mythbreak.repository.ApplicationRepository;
import com.mythbreak.repository.EarnerProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MatchingService Unit Tests — Core Skill Matching Algorithm")
class MatchingServiceTest {

    @Mock
    private EarnerProfileRepository earnerProfileRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private OpportunityService opportunityService;

    @InjectMocks
    private MatchingService matchingService;

    private User testUser;
    private EarnerProfile earnerProfile;
    private Opportunity opportunity;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .role(Role.EARNER)
                .build();

        earnerProfile = EarnerProfile.builder()
                .id(1L)
                .user(testUser)
                .skills(new HashSet<>())
                .build();

        opportunity = new Opportunity();
        opportunity.setId(1L);
        opportunity.setTitle("Java Developer");
        opportunity.setRequiredSkills(new HashSet<>());
    }

    // ---- Helper methods ----

    private Skill createSkill(Long id, String name, String category) {
        Skill skill = new Skill();
        skill.setId(id);
        skill.setName(name);
        skill.setCategory(category);
        return skill;
    }

    private Set<Skill> skills(String... names) {
        Set<Skill> set = new HashSet<>();
        for (int i = 0; i < names.length; i++) {
            set.add(createSkill((long) i, names[i], "Test"));
        }
        return set;
    }

    // ---- Test Cases ----

    @Test
    @DisplayName("100% match — earner has ALL required skills")
    void calculateMatch_AllSkillsMatch_Returns100Percent() {
        opportunity.setRequiredSkills(skills("Java", "Spring Boot", "SQL", "Git"));
        earnerProfile.setSkills(skills("Java", "Spring Boot", "SQL", "Git"));

        MatchResult result = matchingService.calculateMatch(earnerProfile, opportunity);

        assertThat(result.getMatchPercentage()).isEqualTo(100.0);
        assertThat(result.getMatchedSkills()).hasSize(4);
        assertThat(result.getMissingSkills()).isEmpty();
        assertThat(result.getTotalRequiredSkills()).isEqualTo(4);
    }

    @Test
    @DisplayName("50% match — earner has exactly half of required skills")
    void calculateMatch_HalfSkillsMatch_Returns50Percent() {
        opportunity.setRequiredSkills(skills("Java", "Python", "SQL", "Git"));
        earnerProfile.setSkills(skills("Java", "Python"));

        MatchResult result = matchingService.calculateMatch(earnerProfile, opportunity);

        assertThat(result.getMatchPercentage()).isEqualTo(50.0);
        assertThat(result.getMatchedSkills()).containsExactlyInAnyOrder("java", "python");
        assertThat(result.getMissingSkills()).containsExactlyInAnyOrder("sql", "git");
        assertThat(result.getMatchedCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("0% match — earner has NO required skills")
    void calculateMatch_NoSkillsMatch_Returns0Percent() {
        opportunity.setRequiredSkills(skills("Java", "Spring Boot", "SQL", "Git"));
        earnerProfile.setSkills(skills("Photography", "Video Editing"));

        MatchResult result = matchingService.calculateMatch(earnerProfile, opportunity);

        assertThat(result.getMatchPercentage()).isEqualTo(0.0);
        assertThat(result.getMatchedSkills()).isEmpty();
        assertThat(result.getMissingSkills()).hasSize(4);
    }

    @Test
    @DisplayName("0% match — earner has no skills at all")
    void calculateMatch_EarnerHasNoSkills_Returns0Percent() {
        opportunity.setRequiredSkills(skills("Java", "Python"));
        earnerProfile.setSkills(new HashSet<>());

        MatchResult result = matchingService.calculateMatch(earnerProfile, opportunity);

        assertThat(result.getMatchPercentage()).isEqualTo(0.0);
        assertThat(result.getMatchedSkills()).isEmpty();
        assertThat(result.getMissingSkills()).hasSize(2);
    }

    @Test
    @DisplayName("100% match — no required skills means perfect match")
    void calculateMatch_NoRequiredSkills_Returns100Percent() {
        opportunity.setRequiredSkills(new HashSet<>());
        earnerProfile.setSkills(skills("Java", "Python"));

        MatchResult result = matchingService.calculateMatch(earnerProfile, opportunity);

        assertThat(result.getMatchPercentage()).isEqualTo(100.0);
        assertThat(result.getTotalRequiredSkills()).isEqualTo(0);
    }

    @Test
    @DisplayName("Case-insensitive matching — 'java' matches 'Java' matches 'JAVA'")
    void calculateMatch_CaseInsensitive_MatchesCorrectly() {
        opportunity.setRequiredSkills(skills("JAVA", "SPRING BOOT"));
        earnerProfile.setSkills(skills("java", "spring boot"));

        MatchResult result = matchingService.calculateMatch(earnerProfile, opportunity);

        assertThat(result.getMatchPercentage()).isEqualTo(100.0);
        assertThat(result.getMissingSkills()).isEmpty();
    }

    @Test
    @DisplayName("75% match — 3 out of 4 required skills present")
    void calculateMatch_3of4Skills_Returns75Percent() {
        opportunity.setRequiredSkills(skills("Java", "Spring Boot", "MySQL", "Git"));
        earnerProfile.setSkills(skills("Java", "Spring Boot", "MySQL"));

        MatchResult result = matchingService.calculateMatch(earnerProfile, opportunity);

        assertThat(result.getMatchPercentage()).isEqualTo(75.0);
        assertThat(result.getMatchedCount()).isEqualTo(3);
        assertThat(result.getMissingSkills()).contains("git");
    }

    @Test
    @DisplayName("Result contains correct earner info")
    void calculateMatch_ReturnsCorrectEarnerInfo() {
        opportunity.setRequiredSkills(skills("Java"));
        earnerProfile.setSkills(skills("Java"));

        MatchResult result = matchingService.calculateMatch(earnerProfile, opportunity);

        assertThat(result.getEarnerId()).isEqualTo(1L);
        assertThat(result.getEarnerName()).isEqualTo("John Doe");
        assertThat(result.getEarnerEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("rankAllEarners returns list sorted by match percentage descending")
    void rankAllEarners_ReturnsSortedByMatchDesc() {
        // Earner 1 — 75% match (Java, Spring Boot, SQL ✓; Git ✗)
        User user2 = User.builder().id(2L).firstName("Alice").lastName("Smith")
                .email("alice@example.com").role(Role.EARNER).build();
        EarnerProfile earner2 = EarnerProfile.builder()
                .id(2L).user(user2).skills(skills("Java", "Spring Boot", "SQL")).build();

        // Earner 1 (earnerProfile) — 50% match (Java, Python ✓; SQL, Git ✗)
        opportunity.setRequiredSkills(skills("Java", "Spring Boot", "SQL", "Git"));
        earnerProfile.setSkills(skills("Java", "Python"));

        when(earnerProfileRepository.findAll()).thenReturn(List.of(earnerProfile, earner2));
        when(opportunityService.findOrThrow(1L)).thenReturn(opportunity);

        List<MatchResult> results = matchingService.rankAllEarners(1L);

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getMatchPercentage())
                .isGreaterThan(results.get(1).getMatchPercentage());
    }
}
