package com.ascoop.taskmanager.repository;

import com.ascoop.taskmanager.model.Task;
import com.ascoop.taskmanager.model.User;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> getTasksByUserId(Long user_id);
    Task getTaskById(int id);
    void deleteTaskById(int id);
   //void saveTaskByUserId(Long user_id, Task task);
}
