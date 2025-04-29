package com.ascoop.taskmanager.services;

import com.ascoop.taskmanager.model.Task;

import java.util.List;

public interface TaskServices {
    public List<Task> getTasks();
    public void addTask(Task task);
    public void deleteTask(int id);
    public Task getTaskById(int id);
    public  List<Task> getTaskByUserId(Long userId);
}
