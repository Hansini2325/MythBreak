package com.mythbreak.service;

import com.mythbreak.dto.LearnerProfileRequest;
import com.mythbreak.dto.LearnerProfileResponse;
import com.mythbreak.entity.LearnerProfile;
import com.mythbreak.entity.User;
import com.mythbreak.exception.ResourceNotFoundException;
import com.mythbreak.repository.LearnerProfileRepository;
import com.mythbreak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing learner profiles.
 */
@Service
@RequiredArgsConstructor
public class LearnerService {

    private final LearnerProfileRepository learnerProfileRepository;
    private final UserRepository userRepository;

    public LearnerProfileResponse getProfile(Long userId) {
        LearnerProfile profile = learnerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("LearnerProfile", "userId", userId));
        return toResponse(profile);
    }

    @Transactional
    public LearnerProfileResponse createOrUpdateProfile(Long userId, LearnerProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        LearnerProfile profile = learnerProfileRepository.findByUserId(userId)
                .orElse(LearnerProfile.builder().user(user).build());

        profile.setBio(request.getBio());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setCity(request.getCity());
        profile.setCountry(request.getCountry());
        profile.setProfileImageUrl(request.getProfileImageUrl());
        profile.setLinkedinUrl(request.getLinkedinUrl());
        profile.setGithubUrl(request.getGithubUrl());

        return toResponse(learnerProfileRepository.save(profile));
    }

    public LearnerProfileResponse toResponse(LearnerProfile profile) {
        User user = profile.getUser();
        return LearnerProfileResponse.builder()
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
                .build();
    }
}
