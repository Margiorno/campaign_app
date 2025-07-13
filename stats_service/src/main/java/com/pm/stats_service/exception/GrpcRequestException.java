package com.pm.stats_service.exception;

public class GrpcRequestException extends RuntimeException {
    public GrpcRequestException(String message) {
        super(message);
    }
}
