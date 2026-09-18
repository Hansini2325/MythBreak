package com.mythbreak.service;

import com.mythbreak.dto.EarnerProfileRequest;
import com.mythbreak.dto.EarnerProfileResponse;
import com.mythbreak.dto.SkillRequest;
import com.mythbreak.dto.SkillResponse;
import com.mythbreak.entity.EarnerProfile;
import com.mythbreak.entity.Skill;
import com.mythbreak.entity.User;
import com.mythbreak.exception.ResourceNotFoundException;
import com.mythbreak.repository.EarnerProfileRepository;
import com.mythbreak.repository.SkillRepository;
import com.mythbreak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing earner profiles and their skills.
 */
@Service
@RequiredArgsConstructor
public class EarnerService {

    private final EarnerProfileRepository earnerProfileRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final SkillService skillService;

    public EarnerProfileResponse getProfile(Long userId) {
        EarnerProfile profile = earnerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("EarnerProfile", "userId", userId));
        return toResponse(profile);
    }

    @Transactional
    public EarnerProfileResponse createOrUpdateProfile(Long userId, EarnerProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        EarnerProfile profile = earnerProfileRepository.findByUserId(userId)
                .orElse(EarnerProfile.builder().user(user).build());

        profile.setBio(request.getBio());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setCity(request.getCity());
        profile.setCountry(request.getCountry());
        profile.setProfileImageUrl(request.getProfileImageUrl());
        profile.setLinkedinUrl(request.getLinkedinUrl());
        profile.setGithubUrl(request.getGithubUrl());
        profile.setPortfolioUrl(request.getPortfolioUrl());
        profile.setResumeUrl(request.getResumeUrl());
        profile.setYearsOfExperience(request.getYearsOfExperience());

        return toResponse(earnerProfileRepository.save(profile));
    }

    @Transactional
    public EarnerProfileResponse addSkill(Long userId, Long skillId) {
        EarnerProfile profile = earnerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("EarnerProfile", "userId", userId));
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", skillId));

        profile.getSkills().add(skill);
        return toResponse(earnerProfileRepository.save(profile));
    }

    @Transactional
    public EarnerProfileResponse removeSkill(Long userId, Long skillId) {
        EarnerProfile profile = earnerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("EarnerProfile", "userId", userId));

        profile.getSkills().removeIf(s -> s.getId().equals(skillId));
        return toResponse(earnerProfileRepository.save(profile));
    }

    public List<SkillResponse> getEarnerSkills(Long userId) {
        EarnerProfile profile = earnerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("EarnerProfile", "userId", userId));
        return profile.getSkills().stream().map(skillService::toResponse).toList();
    }

    public EarnerProfileResponse toResponse(EarnerProfile profile) {
        User user = profile.getUser();
        return EarnerProfileResponse.builder()
                .id(profile.getId())
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .bio(profile.getBio())
                .phoneNumber(profile.getPhoneNumber())
                .city(profile.getCity())
                .country(profile.getCountry())
                .profileImageUrl(profile.getProfileImageUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .githubUrl(profile.getGithubUrl())
                .portfolioUrl(profile.getPortfolioUrl())
                .resumeUrl(profile.getResumeUrl())
                .yearsOfExperience(profile.getYearsOfExperience())
                .skills(profile.getSkills().stream().map(skillService::toResponse).toList())
                .build();
    }

    /** Helper used by MatchingService to get EarnerProfile by userId */
    public EarnerProfile getEarnerEntityByUserId(Long userId) {
        return earnerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("EarnerProfile", "userId", userId));
    }
}
