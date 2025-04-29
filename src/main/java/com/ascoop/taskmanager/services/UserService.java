package com.ascoop.taskmanager.services;

import com.ascoop.taskmanager.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    User getUser(Long id);
    void saveUser(User user);
}
