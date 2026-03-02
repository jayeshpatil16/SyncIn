package com.example.SyncIn.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserException(UserAlreadyExistsException ex)
    {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("field", ex.getField());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}