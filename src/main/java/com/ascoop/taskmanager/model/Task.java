package com.ascoop.taskmanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  int id;
    private String title;
    private String description;
    private boolean completed;
    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;
}
