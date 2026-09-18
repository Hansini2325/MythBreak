package com.mythbreak.service;

import com.mythbreak.dto.OpportunityRequest;
import com.mythbreak.dto.OpportunityResponse;
import com.mythbreak.dto.SkillResponse;
import com.mythbreak.entity.CompanyProfile;
import com.mythbreak.entity.Opportunity;
import com.mythbreak.entity.Skill;
import com.mythbreak.enums.OpportunityStatus;
import com.mythbreak.exception.ResourceNotFoundException;
import com.mythbreak.exception.UnauthorizedException;
import com.mythbreak.repository.ApplicationRepository;
import com.mythbreak.repository.CompanyProfileRepository;
import com.mythbreak.repository.OpportunityRepository;
import com.mythbreak.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service for managing opportunities posted by companies.
 */
@Service
@RequiredArgsConstructor
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final CompanyProfileRepository companyProfileRepository;
    private final SkillRepository skillRepository;
    private final SkillService skillService;
    private final ApplicationRepository applicationRepository;

    public List<OpportunityResponse> getAllOpenOpportunities() {
        return opportunityRepository.findByStatus(OpportunityStatus.OPEN).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<OpportunityResponse> searchOpportunities(String keyword) {
        return opportunityRepository.searchOpenOpportunities(keyword).stream()
                .map(this::toResponse)
                .toList();
    }

    public OpportunityResponse getOpportunityById(Long id) {
        return toResponse(findOrThrow(id));
    }

    public List<OpportunityResponse> getOpportunitiesByCompany(Long userId) {
        CompanyProfile company = getCompanyOrThrow(userId);
        return opportunityRepository.findByCompanyId(company.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public OpportunityResponse createOpportunity(Long userId, OpportunityRequest request) {
        CompanyProfile company = getCompanyOrThrow(userId);
        Set<Skill> skills = resolveSkills(request.getRequiredSkillIds());

        Opportunity opportunity = Opportunity.builder()
                .company(company)
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .location(request.getLocation())
                .salaryRange(request.getSalaryRange())
                .deadline(request.getDeadline())
                .status(OpportunityStatus.OPEN)
                .requiredSkills(skills)
                .build();

        return toResponse(opportunityRepository.save(opportunity));
    }

    @Transactional
    public OpportunityResponse updateOpportunity(Long userId, Long opportunityId,
                                                  OpportunityRequest request) {
        Opportunity opportunity = findOrThrow(opportunityId);
        verifyOwnership(userId, opportunity);

        Set<Skill> skills = resolveSkills(request.getRequiredSkillIds());

        opportunity.setTitle(request.getTitle());
        opportunity.setDescription(request.getDescription());
        opportunity.setType(request.getType());
        opportunity.setLocation(request.getLocation());
        opportunity.setSalaryRange(request.getSalaryRange());
        opportunity.setDeadline(request.getDeadline());
        opportunity.setRequiredSkills(skills);

        return toResponse(opportunityRepository.save(opportunity));
    }

    @Transactional
    public OpportunityResponse closeOpportunity(Long userId, Long opportunityId) {
        Opportunity opportunity = findOrThrow(opportunityId);
        verifyOwnership(userId, opportunity);
        opportunity.setStatus(OpportunityStatus.CLOSED);
        return toResponse(opportunityRepository.save(opportunity));
    }

    @Transactional
    public void deleteOpportunity(Long userId, Long opportunityId) {
        Opportunity opportunity = findOrThrow(opportunityId);
        verifyOwnership(userId, opportunity);
        opportunityRepository.delete(opportunity);
    }

    // ---- Helpers ----

    public Opportunity findOrThrow(Long id) {
        return opportunityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Opportunity", "id", id));
    }

    private CompanyProfile getCompanyOrThrow(Long userId) {
        return companyProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CompanyProfile", "userId", userId));
    }

    private void verifyOwnership(Long userId, Opportunity opportunity) {
        CompanyProfile company = getCompanyOrThrow(userId);
        if (!opportunity.getCompany().getId().equals(company.getId())) {
            throw new UnauthorizedException("You are not the owner of this opportunity.");
        }
    }

    private Set<Skill> resolveSkills(List<Long> skillIds) {
        if (skillIds == null || skillIds.isEmpty()) return new HashSet<>();
        return new HashSet<>(skillRepository.findAllById(skillIds));
    }

    public OpportunityResponse toResponse(Opportunity o) {
        long appCount = applicationRepository.findByOpportunityId(o.getId()).size();
        return OpportunityResponse.builder()
                .id(o.getId())
                .title(o.getTitle())
                .description(o.getDescription())
                .type(o.getType())
                .location(o.getLocation())
                .salaryRange(o.getSalaryRange())
                .deadline(o.getDeadline())
                .status(o.getStatus())
                .createdAt(o.getCreatedAt())
                .companyId(o.getCompany().getId())
                .companyName(o.getCompany().getCompanyName())
                .companyLogoUrl(o.getCompany().getLogoUrl())
                .requiredSkills(o.getRequiredSkills().stream()
                        .map(skillService::toResponse)
                        .toList())
                .applicationCount(appCount)
                .build();
    }
}
