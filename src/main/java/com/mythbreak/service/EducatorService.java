package com.mythbreak.service;

import com.mythbreak.dto.EducatorProfileRequest;
import com.mythbreak.dto.EducatorProfileResponse;
import com.mythbreak.entity.EducatorProfile;
import com.mythbreak.entity.User;
import com.mythbreak.exception.ResourceNotFoundException;
import com.mythbreak.repository.EducatorProfileRepository;
import com.mythbreak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing educator profiles.
 */
@Service
@RequiredArgsConstructor
public class EducatorService {

    private final EducatorProfileRepository educatorProfileRepository;
    private final UserRepository userRepository;

    public EducatorProfileResponse getProfile(Long userId) {
        EducatorProfile profile = educatorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("EducatorProfile", "userId", userId));
        return toResponse(profile);
    }

    @Transactional
    public EducatorProfileResponse createOrUpdateProfile(Long userId, EducatorProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        EducatorProfile profile = educatorProfileRepository.findByUserId(userId)
                .orElse(EducatorProfile.builder().user(user).build());

        profile.setBio(request.getBio());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setCity(request.getCity());
        profile.setCountry(request.getCountry());
        profile.setProfileImageUrl(request.getProfileImageUrl());
        profile.setLinkedinUrl(request.getLinkedinUrl());
        profile.setWebsiteUrl(request.getWebsiteUrl());
        profile.setExpertise(request.getExpertise());
        profile.setQualification(request.getQualification());
        profile.setYearsOfExperience(request.getYearsOfExperience());

        return toResponse(educatorProfileRepository.save(profile));
    }

    public EducatorProfile getEducatorEntityByUserId(Long userId) {
        return educatorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("EducatorProfile", "userId", userId));
    }

    public EducatorProfileResponse toResponse(EducatorProfile profile) {
        User user = profile.getUser();
        return EducatorProfileResponse.builder()
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
                .websiteUrl(profile.getWebsiteUrl())
                .expertise(profile.getExpertise())
                .qualification(profile.getQualification())
                .yearsOfExperience(profile.getYearsOfExperience())
                .build();
    }
}
