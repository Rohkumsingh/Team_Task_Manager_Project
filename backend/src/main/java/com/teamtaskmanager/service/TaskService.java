package com.teamtaskmanager.service;

import com.teamtaskmanager.dto.TaskRequest;
import com.teamtaskmanager.entity.Project;
import com.teamtaskmanager.entity.Role;
import com.teamtaskmanager.entity.Task;
import com.teamtaskmanager.entity.TaskStatus;
import com.teamtaskmanager.entity.User;
import com.teamtaskmanager.exception.BadRequestException;
import com.teamtaskmanager.exception.NotFoundException;
import com.teamtaskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, ProjectService projectService, UserService userService) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
        this.userService = userService;
    }

    public Task createTask(Long projectId, TaskRequest request) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            throw new BadRequestException("Only ADMIN can create tasks");
        }

        Project project = projectService.getProject(projectId);
        User assignee = userService.getById(request.getAssignedToUserId());

        if (project.getMembers().stream().noneMatch(u -> u.getId().equals(assignee.getId()))) {
            throw new BadRequestException("Assignee must be a project member");
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setPriority(request.getPriority());
        task.setProject(project);
        task.setAssignedTo(assignee);

        return taskRepository.save(task);
    }

    public List<Task> getProjectTasks(Long projectId) {
        User currentUser = userService.getCurrentUser();
        Project project = projectService.getProject(projectId);
        boolean inProject = project.getMembers().stream().anyMatch(u -> u.getId().equals(currentUser.getId()));
        if (!inProject) {
            throw new BadRequestException("You are not a member of this project");
        }
        return taskRepository.findByProjectId(projectId);
    }

    public List<Task> getMyTasks() {
        return taskRepository.findByAssignedTo(userService.getCurrentUser());
    }

    public Task updateMyTaskStatus(Long taskId, TaskStatus status) {
        User currentUser = userService.getCurrentUser();
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));

        if (!task.getAssignedTo().getId().equals(currentUser.getId())) {
            throw new BadRequestException("Only assigned user can update this task");
        }

        task.setStatus(status);
        return taskRepository.save(task);
    }
}
