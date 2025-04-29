package com.ascoop.taskmanager.services.impl;

import com.ascoop.taskmanager.model.Task;
import com.ascoop.taskmanager.repository.TaskRepository;
import com.ascoop.taskmanager.services.TaskServices;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ITaskService implements TaskServices {
   private final TaskRepository taskRepository;

    @Override
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(int id) {
        return taskRepository.getTaskById(id);
    }

    @Override
    public void addTask(Task task) {
        try {

            taskRepository.save(task);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//@Override
/*   public void saveTaskByUserId(Task task) {
        taskRepository.saveTaskByUserId(task.getUser().getId(), task);
    }*/

    @Override
    @Transactional
    public void deleteTask(int id) {
        taskRepository.deleteTaskById(id);
    }

    @Override
    public List<Task> getTaskByUserId(Long userId) {
        return taskRepository.getTasksByUserId(userId);
    }
}
