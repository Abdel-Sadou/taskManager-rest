package com.ascoop.taskmanager.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 🚀 Gestion des erreurs d'authentification
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password", e);
    }

    @ExceptionHandler(AccountStatusException.class)
    public ResponseEntity<Map<String, Object>> handleAccountStatusException(AccountStatusException e) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Account is locked or disabled", e);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Map<String, Object>> handleDisabledException(DisabledException e) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Account is disabled", e);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Map<String, Object>> handleLockedException(LockedException e) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Account is locked", e);
    }

    // 🚀 Gestion des erreurs de validation (ex: JSON mal formé, champ manquant)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("error", "Validation failed");

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        fieldErrors.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        logger.warn("Validation error: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // 🚀 Gestion des autres erreurs
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        e.printStackTrace();
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", e);
    }

    // 📌 Fonction pour construire la réponse d'erreur
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message, Exception e) {
        String errorId = UUID.randomUUID().toString(); // Un identifiant unique pour l'erreur
        logger.error("Error [{}]: {}", errorId, e.getMessage(), e);

        Map<String, Object> response = Map.of(
                "error", status.getReasonPhrase(),
                "message", message,
                "errorId", errorId
        );
        return ResponseEntity.status(status).body(response);
    }
}
