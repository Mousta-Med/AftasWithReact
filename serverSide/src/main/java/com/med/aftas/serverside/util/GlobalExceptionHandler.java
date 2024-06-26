package com.med.aftas.serverside.util;

import com.med.aftas.serverside.exception.CompetitionDateValidationException;
import com.med.aftas.serverside.exception.PointsValidationException;
import com.med.aftas.serverside.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handelInvalidArgument(MethodArgumentNotValidException exception) {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PointsValidationException.class)
    public Map<String, String> handlePointsValidationException(PointsValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handelException(IllegalStateException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CompetitionDateValidationException.class)
    public ResponseEntity<?> handelCompetitionDateValidationException(CompetitionDateValidationException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handelException() {
        return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public Map<String, String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return errors;
    }
}
