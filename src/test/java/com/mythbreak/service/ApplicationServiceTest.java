package com.mythbreak.service;

import com.mythbreak.dto.ApplicationRequest;
import com.mythbreak.dto.ApplicationResponse;
import com.mythbreak.entity.*;
import com.mythbreak.enums.*;
import com.mythbreak.exception.BadRequestException;
import com.mythbreak.exception.DuplicateResourceException;
import com.mythbreak.repository.ApplicationRepository;
import com.mythbreak.repository.EarnerProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplicationService Unit Tests")
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private EarnerProfileRepository earnerProfileRepository;

    @Mock
    private OpportunityService opportunityService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ApplicationService applicationService;

    private User earnerUser;
    private EarnerProfile earnerProfile;
    private CompanyProfile companyProfile;
    private User companyUser;
    private Opportunity opportunity;
    private ApplicationRequest applicationRequest;

    @BeforeEach
    void setUp() {
        earnerUser = User.builder()
                .id(1L).firstName("Arjun").lastName("Nair")
                .email("arjun@earner.com").role(Role.EARNER).build();

        earnerProfile = EarnerProfile.builder()
                .id(1L).user(earnerUser).skills(new HashSet<>()).build();

        companyUser = User.builder()
                .id(2L).firstName("HR").lastName("TechCorp")
                .email("hr@techcorp.com").role(Role.COMPANY).build();

        companyProfile = CompanyProfile.builder()
                .id(1L).user(companyUser).companyName("TechCorp").build();

        opportunity = new Opportunity();
        opportunity.setId(1L);
        opportunity.setTitle("Java Developer");
        opportunity.setStatus(OpportunityStatus.OPEN);
        opportunity.setDeadline(LocalDate.now().plusDays(30));
        opportunity.setCompany(companyProfile);
        opportunity.setRequiredSkills(new HashSet<>());

        applicationRequest = new ApplicationRequest();
        applicationRequest.setCoverLetter("I am a great candidate!");
    }

    @Test
    @DisplayName("Apply - successful application returns response")
    void apply_ValidApplication_ReturnsResponse() {
        when(earnerProfileRepository.findByUserId(1L)).thenReturn(Optional.of(earnerProfile));
        when(opportunityService.findOrThrow(1L)).thenReturn(opportunity);
        when(applicationRepository.existsByEarnerIdAndOpportunityId(1L, 1L)).thenReturn(false);

        Application saved = Application.builder()
                .id(1L).earner(earnerProfile).opportunity(opportunity)
                .status(ApplicationStatus.APPLIED).coverLetter("I am a great candidate!")
                .build();

        when(applicationRepository.save(any(Application.class))).thenReturn(saved);

        ApplicationResponse response = applicationService.apply(1L, 1L, applicationRequest);

        assertThat(response.getOpportunityTitle()).isEqualTo("Java Developer");
        assertThat(response.getStatus()).isEqualTo(ApplicationStatus.APPLIED);
        verify(notificationService).createNotification(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Apply - duplicate application throws DuplicateResourceException")
    void apply_DuplicateApplication_ThrowsException() {
        when(earnerProfileRepository.findByUserId(1L)).thenReturn(Optional.of(earnerProfile));
        when(opportunityService.findOrThrow(1L)).thenReturn(opportunity);
        when(applicationRepository.existsByEarnerIdAndOpportunityId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> applicationService.apply(1L, 1L, applicationRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already applied");

        verify(applicationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Apply - after deadline throws BadRequestException")
    void apply_AfterDeadline_ThrowsBadRequestException() {
        opportunity.setDeadline(LocalDate.now().minusDays(1)); // Past deadline
        when(earnerProfileRepository.findByUserId(1L)).thenReturn(Optional.of(earnerProfile));
        when(opportunityService.findOrThrow(1L)).thenReturn(opportunity);

        assertThatThrownBy(() -> applicationService.apply(1L, 1L, applicationRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("deadline");

        verify(applicationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Apply - closed opportunity throws BadRequestException")
    void apply_ClosedOpportunity_ThrowsBadRequestException() {
        opportunity.setStatus(OpportunityStatus.CLOSED);
        when(earnerProfileRepository.findByUserId(1L)).thenReturn(Optional.of(earnerProfile));
        when(opportunityService.findOrThrow(1L)).thenReturn(opportunity);

        assertThatThrownBy(() -> applicationService.apply(1L, 1L, applicationRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no longer accepting");

        verify(applicationRepository, never()).save(any());
    }
}
