package com.teamtaskmanager.service;

import com.teamtaskmanager.entity.TaskStatus;
import com.teamtaskmanager.entity.User;
import com.teamtaskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public DashboardService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public Map<String, Object> getMyDashboard() {
        User user = userService.getCurrentUser();
        Map<String, Object> data = new HashMap<>();

        long total = taskRepository.countByAssignedTo(user);
        long todo = taskRepository.countByAssignedToAndStatus(user, TaskStatus.TODO);
        long inProgress = taskRepository.countByAssignedToAndStatus(user, TaskStatus.IN_PROGRESS);
        long done = taskRepository.countByAssignedToAndStatus(user, TaskStatus.DONE);
        long overdue = taskRepository.countByAssignedToAndDueDateBeforeAndStatusNot(user, LocalDate.now(), TaskStatus.DONE);

        data.put("totalTasks", total);
        data.put("tasksByStatus", Map.of("TODO", todo, "IN_PROGRESS", inProgress, "DONE", done));
        data.put("tasksPerUser", Map.of(user.getName(), total));
        data.put("overdueTasks", overdue);

        return data;
    }
}
