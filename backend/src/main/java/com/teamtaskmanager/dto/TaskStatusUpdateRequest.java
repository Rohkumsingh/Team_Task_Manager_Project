package com.teamtaskmanager.dto;

import com.teamtaskmanager.entity.TaskStatus;

public class TaskStatusUpdateRequest {
    private TaskStatus status;

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
}
