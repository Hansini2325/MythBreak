package com.mythbreak.service;

import com.mythbreak.dto.CompanyProfileRequest;
import com.mythbreak.dto.CompanyProfileResponse;
import com.mythbreak.entity.CompanyProfile;
import com.mythbreak.entity.User;
import com.mythbreak.exception.ResourceNotFoundException;
import com.mythbreak.repository.CompanyProfileRepository;
import com.mythbreak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing company profiles.
 */
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyProfileRepository companyProfileRepository;
    private final UserRepository userRepository;

    public CompanyProfileResponse getProfile(Long userId) {
        CompanyProfile profile = companyProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CompanyProfile", "userId", userId));
        return toResponse(profile);
    }

    @Transactional
    public CompanyProfileResponse createOrUpdateProfile(Long userId, CompanyProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        CompanyProfile profile = companyProfileRepository.findByUserId(userId)
                .orElse(CompanyProfile.builder().user(user).build());

        if (request.getCompanyName() != null) {
            profile.setCompanyName(request.getCompanyName());
        }
        profile.setDescription(request.getDescription());
        profile.setIndustry(request.getIndustry());
        profile.setWebsite(request.getWebsite());
        profile.setLogoUrl(request.getLogoUrl());
        profile.setCity(request.getCity());
        profile.setCountry(request.getCountry());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setCompanySize(request.getCompanySize());

        return toResponse(companyProfileRepository.save(profile));
    }

    public CompanyProfile getCompanyEntityByUserId(Long userId) {
        return companyProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CompanyProfile", "userId", userId));
    }

    public CompanyProfileResponse toResponse(CompanyProfile profile) {
        User user = profile.getUser();
        return CompanyProfileResponse.builder()
                .id(profile.getId())
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .companyName(profile.getCompanyName())
                .description(profile.getDescription())
                .industry(profile.getIndustry())
                .website(profile.getWebsite())
                .logoUrl(profile.getLogoUrl())
                .city(profile.getCity())
                .country(profile.getCountry())
                .phoneNumber(profile.getPhoneNumber())
                .companySize(profile.getCompanySize())
                .build();
    }
}
