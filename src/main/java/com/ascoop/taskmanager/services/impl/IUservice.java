package com.ascoop.taskmanager.services.impl;

import com.ascoop.taskmanager.model.User;
import com.ascoop.taskmanager.repository.UserRepository;
import com.ascoop.taskmanager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class IUservice implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository ;


    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public Long saveUser(User user)  {
       /* if (userRepository.existsByEmail(user.getEmail())) {
            throw  new RuntimeException("Email Already Exists");
        }*/
        User userSave =   userRepository.findByEmail(user.getEmail()).orElseGet(() -> {
             if (user.getPassword() == null) {
                 user.setPassword(null);
             } else {
                 user.setPassword(passwordEncoder.encode(user.getPassword()));
             }
            System.out.println(user);
            System.out.println("*******************************************************  userRepository.findByEmail erRepository.save **************");
                return userRepository.save(user);
            });

        return userSave.getId();
    }
}
