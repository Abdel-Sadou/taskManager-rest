package com.ascoop.taskmanager.api;

import com.ascoop.taskmanager.config.AuthService;
import com.ascoop.taskmanager.dto.AuthRequest;
import com.ascoop.taskmanager.model.User;
import com.ascoop.taskmanager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class CAuthentication {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("auth")
    public ResponseEntity<Map<String, Object>> auth(@RequestBody AuthRequest authRequest) {
        System.out.println("**********");
        System.out.println(authRequest);
          return ResponseEntity.ok(authService.generateAccessToken(authRequest.getUsername(), authRequest.getPassword()));
    }
    @GetMapping("hello")
    public ResponseEntity<String> auth() {
          return ResponseEntity.ok("Hello");
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
            userService.saveUser(user);
            return ResponseEntity.ok("User registered successfully"+user.getUsername());
    }
}
