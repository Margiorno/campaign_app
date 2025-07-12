package com.pm.campaign_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
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
}
