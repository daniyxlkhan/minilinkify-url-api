package com.example.minilinkify.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> invalidURL(MethodArgumentNotValidException exception) {
        List<String> errors = new ArrayList<>();

        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String message = fieldError.getField() + " " + fieldError.getDefaultMessage();
            errors.add(message);
        });

        Map<String, Object> statusAndErrors = new LinkedHashMap<>();
        statusAndErrors.put("status", 400);
        statusAndErrors.put("errors", errors);

        return ResponseEntity.badRequest().body(statusAndErrors);
    }
}
