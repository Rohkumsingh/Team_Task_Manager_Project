package com.teamtaskmanager.service;

import com.teamtaskmanager.dto.ProjectRequest;
import com.teamtaskmanager.entity.Project;
import com.teamtaskmanager.entity.Role;
import com.teamtaskmanager.entity.User;
import com.teamtaskmanager.exception.BadRequestException;
import com.teamtaskmanager.exception.NotFoundException;
import com.teamtaskmanager.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public ProjectService(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    public Project createProject(ProjectRequest request) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            throw new BadRequestException("Only ADMIN can create projects");
        }

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.getMembers().add(currentUser);
        return projectRepository.save(project);
    }

    public List<Project> getMyProjects() {
        return projectRepository.findByMembersContaining(userService.getCurrentUser());
    }

    public Project addMember(Long projectId, Long userId) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            throw new BadRequestException("Only ADMIN can manage members");
        }

        Project project = getProject(projectId);
        project.getMembers().add(userService.getById(userId));
        return projectRepository.save(project);
    }

    public Project removeMember(Long projectId, Long userId) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            throw new BadRequestException("Only ADMIN can manage members");
        }

        Project project = getProject(projectId);
        project.getMembers().removeIf(u -> u.getId().equals(userId));
        return projectRepository.save(project);
    }

    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
    }
}
