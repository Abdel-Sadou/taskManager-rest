package com.ascoop.taskmanager.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class JwtExceptionHandler {

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, Object>> handleJwtException(JwtException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", Instant.now());
        error.put("error", "Invalid or malformed JWT token");
        error.put("message", ex.getMessage());

        // Log dans la console pour plus de détails pendant le dev
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

}
