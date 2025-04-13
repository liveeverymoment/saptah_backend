package org.saptah.main.adminuser.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex){
        LinkedHashMap<String, Object> errors = new LinkedHashMap<>();
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        // extract field errors
        for(FieldError error:ex.getBindingResult().getFieldErrors()){
            errors.put(error.getField(),error.getDefaultMessage());
        }

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Validation failed.");
        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDuplicateKey(DataIntegrityViolationException ex) {
        LinkedHashMap<String, Object> errors = new LinkedHashMap<>();
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        // extract field errors
        errors.put("error",ex.getMessage());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("message", "Duplicate email or country code + mobile number.");
        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}
