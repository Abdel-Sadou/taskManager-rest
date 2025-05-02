package com.ascoop.taskmanager.repository;

import com.ascoop.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
