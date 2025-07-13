package com.pm.campaign_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CampaignOperationException.class)
    public ResponseEntity<Map<String,String>> handleCampaignOperationException(CampaignOperationException ex) {
        Map<String,String> map = new HashMap<>();

        map.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(CityOperationException.class)
    public ResponseEntity<Map<String,String>> handleCityOperationException(CityOperationException ex) {
        Map<String,String> map = new HashMap<>();

        map.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(ProductOperationException.class)
    public ResponseEntity<Map<String,String>> handleProductOperationException(ProductOperationException ex) {
        Map<String,String> map = new HashMap<>();

        map.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(InvalidUuidException.class)
    public ResponseEntity<Map<String,String>> handleInvalidUuidException(InvalidUuidException ex) {
        Map<String,String> map = new HashMap<>();

        map.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(GrpcRequestException.class)
    public ResponseEntity<Map<String,String>> handleGrpcRequestException(GrpcRequestException ex) {
        Map<String,String> map = new HashMap<>();

        map.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(map);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
