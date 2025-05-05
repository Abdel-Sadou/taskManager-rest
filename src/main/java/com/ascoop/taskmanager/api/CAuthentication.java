package com.ascoop.taskmanager.api;

import com.ascoop.taskmanager.config.AuthService;
import com.ascoop.taskmanager.dto.AuthRequest;
import com.ascoop.taskmanager.dto.PayloadG;
import com.ascoop.taskmanager.model.User;
import com.ascoop.taskmanager.repository.UserRepository;
import com.ascoop.taskmanager.services.UserService;
import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CAuthentication {
    private final AuthService authService;
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("auth")
    public ResponseEntity<Map<String, Object>> auth(@RequestBody AuthRequest authRequest) {
        System.out.println("**********");
        System.out.println(authRequest);
          return ResponseEntity.ok(authService.generateAccessToken(authRequest.getUsername(), authRequest.getPassword()));
    }
    @PostMapping("auth/google")
    public ResponseEntity<?> authGoogle(@RequestBody PayloadG payload) {
        Jwt decodedJwt;
        try {
            System.out.println("auth google ------------------");
            System.out.println(payload.getIdToken());
            System.out.println("auth google ------------------");
            NimbusJwtDecoder googleDecoder = JwtDecoders.fromIssuerLocation("https://accounts.google.com");
            decodedJwt = googleDecoder.decode(payload.getIdToken());

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        User u = new User();
        // Récupère les infos
       u.setEmail( decodedJwt.getClaimAsString("email"));
       u.setUsername( decodedJwt.getClaimAsString("name"));
       u.setPassword(null);
       u.setRole("USER");
        String sub = decodedJwt.getSubject();

        userRepository.findByEmail(u.getEmail())
                .orElseGet(() -> userRepository.save(u));

        return ResponseEntity.ok(authService.buildAccessToken(u.getEmail(),true));
    }

    @GetMapping("hello")
    public ResponseEntity<String> auth() {
          return ResponseEntity.ok("Hello");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
            return ResponseEntity.ok(userService.saveUser(user));
    }
}
