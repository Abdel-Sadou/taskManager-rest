package com.ascoop.taskmanager.dto;

import com.ascoop.taskmanager.model.Task;
import com.ascoop.taskmanager.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;
@Data
public class UserDTO   {
    private Long id;
    private String username;
    private String role;
    private String email;
    private String password;
    private boolean enabled;
    private List<TaskDTO> tasks;
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.enabled = user.isEnabled();
    }
}
