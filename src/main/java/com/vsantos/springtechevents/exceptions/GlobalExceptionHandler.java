package com.vsantos.springtechevents.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex,
      WebRequest request) {
    Map<String, List<String>> errors = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach(error -> {
      errors.computeIfAbsent(error.getField(), k -> new ArrayList<>()).add(error.getDefaultMessage());
    });

    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("timestamp", LocalDateTime.now());
    response.put("message", "Validation failed");
    response.put("errors", errors);
    response.put("path", request.getDescription(false).replace("uri=", ""));

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
