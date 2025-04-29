package com.ascoop.taskmanager.dto;

import com.ascoop.taskmanager.model.Task;
import com.ascoop.taskmanager.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskDTO {
    private  int id;
    private String title;
    private String description;
    private boolean completed;
    private User user;
    public TaskDTO(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.completed = task.isCompleted();

    }

}
