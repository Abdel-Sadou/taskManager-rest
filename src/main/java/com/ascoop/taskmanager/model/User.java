package com.ascoop.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String role;
    @Column(unique = true)
    private String email;
    private String password;
    private String providerId;
    private boolean enabled;
    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

}
