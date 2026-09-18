package com.mythbreak.service;

import com.mythbreak.dto.ProjectRequest;
import com.mythbreak.dto.ProjectResponse;
import com.mythbreak.entity.EarnerProfile;
import com.mythbreak.entity.Project;
import com.mythbreak.entity.Skill;
import com.mythbreak.exception.ResourceNotFoundException;
import com.mythbreak.exception.UnauthorizedException;
import com.mythbreak.repository.EarnerProfileRepository;
import com.mythbreak.repository.ProjectRepository;
import com.mythbreak.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service for managing earner portfolio projects.
 */
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final EarnerProfileRepository earnerProfileRepository;
    private final SkillRepository skillRepository;
    private final SkillService skillService;

    public List<ProjectResponse> getProjectsByEarner(Long userId) {
        EarnerProfile earner = getEarnerOrThrow(userId);
        return projectRepository.findByEarnerId(earner.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProjectResponse createProject(Long userId, ProjectRequest request) {
        EarnerProfile earner = getEarnerOrThrow(userId);
        Set<Skill> technologies = resolveSkills(request.getSkillIds());

        Project project = Project.builder()
                .earner(earner)
                .title(request.getTitle())
                .description(request.getDescription())
                .projectUrl(request.getProjectUrl())
                .githubUrl(request.getGithubUrl())
                .demoUrl(request.getDemoUrl())
                .completionDate(request.getCompletionDate())
                .technologies(technologies)
                .build();

        return toResponse(projectRepository.save(project));
    }

    @Transactional
    public ProjectResponse updateProject(Long userId, Long projectId, ProjectRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        verifyProjectOwnership(userId, project);

        Set<Skill> technologies = resolveSkills(request.getSkillIds());

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setProjectUrl(request.getProjectUrl());
        project.setGithubUrl(request.getGithubUrl());
        project.setDemoUrl(request.getDemoUrl());
        project.setCompletionDate(request.getCompletionDate());
        project.setTechnologies(technologies);

        return toResponse(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(Long userId, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        verifyProjectOwnership(userId, project);
        projectRepository.delete(project);
    }

    // ---- Helpers ----

    private EarnerProfile getEarnerOrThrow(Long userId) {
        return earnerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("EarnerProfile", "userId", userId));
    }

    private void verifyProjectOwnership(Long userId, Project project) {
        EarnerProfile earner = getEarnerOrThrow(userId);
        if (!project.getEarner().getId().equals(earner.getId())) {
            throw new UnauthorizedException("You are not the owner of this project.");
        }
    }

    private Set<Skill> resolveSkills(List<Long> skillIds) {
        if (skillIds == null || skillIds.isEmpty()) return new HashSet<>();
        return new HashSet<>(skillRepository.findAllById(skillIds));
    }

    public ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .earnerId(project.getEarner().getId())
                .earnerName(project.getEarner().getUser().getFirstName() + " " +
                            project.getEarner().getUser().getLastName())
                .title(project.getTitle())
                .description(project.getDescription())
                .projectUrl(project.getProjectUrl())
                .githubUrl(project.getGithubUrl())
                .demoUrl(project.getDemoUrl())
                .completionDate(project.getCompletionDate())
                .technologies(project.getTechnologies().stream()
                        .map(skillService::toResponse)
                        .toList())
                .build();
    }
}
