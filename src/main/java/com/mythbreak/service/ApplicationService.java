package com.mythbreak.service;

import com.mythbreak.dto.ApplicationRequest;
import com.mythbreak.dto.ApplicationResponse;
import com.mythbreak.entity.Application;
import com.mythbreak.entity.CompanyProfile;
import com.mythbreak.entity.EarnerProfile;
import com.mythbreak.entity.Opportunity;
import com.mythbreak.enums.ApplicationStatus;
import com.mythbreak.enums.NotificationType;
import com.mythbreak.enums.OpportunityStatus;
import com.mythbreak.exception.BadRequestException;
import com.mythbreak.exception.DuplicateResourceException;
import com.mythbreak.exception.ResourceNotFoundException;
import com.mythbreak.exception.UnauthorizedException;
import com.mythbreak.repository.ApplicationRepository;
import com.mythbreak.repository.CompanyProfileRepository;
import com.mythbreak.repository.EarnerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for earners applying to opportunities, and companies managing applications.
 */
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final EarnerProfileRepository earnerProfileRepository;
    private final CompanyProfileRepository companyProfileRepository;
    private final OpportunityService opportunityService;
    private final NotificationService notificationService;

    // ============================================================
    // Earner operations
    // ============================================================

    @Transactional
    public ApplicationResponse apply(
            Long userId,
            Long opportunityId,
            ApplicationRequest request) {

        EarnerProfile earner = earnerProfileRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "EarnerProfile", "userId", userId));

        Opportunity opportunity = opportunityService.findOrThrow(opportunityId);

        if (opportunity.getStatus() != OpportunityStatus.OPEN) {
            throw new BadRequestException(
                    "This opportunity is no longer accepting applications.");
        }

        if (opportunity.getDeadline() != null
                && opportunity.getDeadline().isBefore(LocalDate.now())) {

            throw new BadRequestException(
                    "The application deadline has passed.");
        }

        if (applicationRepository.existsByEarnerIdAndOpportunityId(
                earner.getId(), opportunityId)) {

            throw new DuplicateResourceException(
                    "You have already applied for this opportunity.");
        }

        Application application = Application.builder()
                .earner(earner)
                .opportunity(opportunity)
                .status(ApplicationStatus.APPLIED)
                .coverLetter(request.getCoverLetter())
                .build();

        Application saved = applicationRepository.save(application);

        notificationService.createNotification(
                earner.getUser(),
                "Application Submitted",
                "Your application for '" + opportunity.getTitle()
                        + "' has been received.",
                NotificationType.APPLICATION_SUBMITTED
        );

        return toResponse(saved);
    }

    public List<ApplicationResponse> getEarnerApplications(Long userId) {

        EarnerProfile earner = earnerProfileRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "EarnerProfile", "userId", userId));

        return applicationRepository.findByEarnerId(earner.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ============================================================
    // Company operations
    // ============================================================

    /**
     * Get all applications received by all opportunities
     * belonging to the logged-in company.
     */
    public List<ApplicationResponse> getApplicationsByCompany(Long userId) {

        CompanyProfile company = companyProfileRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "CompanyProfile", "userId", userId));

        return applicationRepository
                .findByOpportunityCompanyId(company.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Get all applications for one specific opportunity.
     */
    public List<ApplicationResponse> getApplicationsForOpportunity(
            Long opportunityId) {

        return applicationRepository.findByOpportunityId(opportunityId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Update application status using opportunity ID + application ID.
     */
    @Transactional
    public ApplicationResponse updateStatus(
            Long opportunityId,
            Long applicationId,
            ApplicationStatus newStatus,
            Long companyUserId) {

        // Make sure the company profile exists.
        CompanyProfile company = companyProfileRepository
                .findByUserId(companyUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "CompanyProfile", "userId", companyUserId));

        Opportunity opportunity =
                opportunityService.findOrThrow(opportunityId);

        // Verify that the opportunity belongs to this company.
        if (!opportunity.getCompany().getId().equals(company.getId())) {
            throw new UnauthorizedException(
                    "You are not authorized to manage this opportunity.");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application", "id", applicationId));

        // Verify that the application belongs to the specified opportunity.
        if (!application.getOpportunity().getId().equals(opportunityId)) {
            throw new BadRequestException(
                    "Application does not belong to this opportunity.");
        }

        application.setStatus(newStatus);

        Application saved = applicationRepository.save(application);

        sendStatusNotification(saved, opportunity, newStatus);

        return toResponse(saved);
    }

    /**
     * Shortcut method used by:
     * PATCH /api/company/applications/{applicationId}/status
     */
    @Transactional
    public ApplicationResponse updateStatusByAppId(
            Long applicationId,
            ApplicationStatus newStatus,
            Long companyUserId) {

        CompanyProfile company = companyProfileRepository
                .findByUserId(companyUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "CompanyProfile", "userId", companyUserId));

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application", "id", applicationId));

        Opportunity opportunity = application.getOpportunity();

        // Verify that this opportunity belongs to the logged-in company.
        if (!opportunity.getCompany().getId().equals(company.getId())) {
            throw new UnauthorizedException(
                    "You are not authorized to update this application.");
        }

        application.setStatus(newStatus);

        Application saved = applicationRepository.save(application);

        sendStatusNotification(saved, opportunity, newStatus);

        return toResponse(saved);
    }

    // ============================================================
    // Notification helper
    // ============================================================

    private void sendStatusNotification(
            Application application,
            Opportunity opportunity,
            ApplicationStatus newStatus) {

        NotificationType notifType = switch (newStatus) {
            case SHORTLISTED -> NotificationType.APPLICATION_SHORTLISTED;
            case REJECTED -> NotificationType.APPLICATION_REJECTED;
            case SELECTED -> NotificationType.APPLICATION_SELECTED;
            default -> NotificationType.GENERAL;
        };

        notificationService.createNotification(
                application.getEarner().getUser(),
                "Application Update",
                "Your application for '" + opportunity.getTitle()
                        + "' is now: " + newStatus.name(),
                notifType
        );
    }

    // ============================================================
    // Response helper
    // ============================================================

    public ApplicationResponse toResponse(Application a) {

        return ApplicationResponse.builder()
                .id(a.getId())
                .earnerId(a.getEarner().getId())
                .earnerName(
                        a.getEarner().getUser().getFirstName()
                                + " "
                                + a.getEarner().getUser().getLastName()
                )
                .earnerEmail(a.getEarner().getUser().getEmail())
                .opportunityId(a.getOpportunity().getId())
                .opportunityTitle(a.getOpportunity().getTitle())
                .companyName(
                        a.getOpportunity()
                                .getCompany()
                                .getCompanyName()
                )
                .appliedAt(a.getAppliedAt())
                .status(a.getStatus())
                .coverLetter(a.getCoverLetter())
                .build();
    }
}