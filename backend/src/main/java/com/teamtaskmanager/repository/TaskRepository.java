package com.teamtaskmanager.repository;

import com.teamtaskmanager.entity.Task;
import com.teamtaskmanager.entity.TaskStatus;
import com.teamtaskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByAssignedTo(User user);
    long countByAssignedTo(User user);
    long countByAssignedToAndStatus(User user, TaskStatus status);
    long countByAssignedToAndDueDateBeforeAndStatusNot(User user, LocalDate date, TaskStatus status);
}
