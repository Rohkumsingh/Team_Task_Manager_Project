package com.teamtaskmanager.controller;

import com.teamtaskmanager.dto.TaskRequest;
import com.teamtaskmanager.dto.TaskStatusUpdateRequest;
import com.teamtaskmanager.entity.Task;
import com.teamtaskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/project/{projectId}")
    public ResponseEntity<Task> createTask(@PathVariable Long projectId, @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(projectId, request));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getProjectTasks(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getProjectTasks(projectId));
    }

    @GetMapping("/me")
    public ResponseEntity<List<Task>> getMyTasks() {
        return ResponseEntity.ok(taskService.getMyTasks());
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<Task> updateMyTaskStatus(@PathVariable Long taskId, @RequestBody TaskStatusUpdateRequest request) {
        return ResponseEntity.ok(taskService.updateMyTaskStatus(taskId, request.getStatus()));
    }
}
