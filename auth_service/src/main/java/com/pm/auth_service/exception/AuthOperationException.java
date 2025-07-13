package com.pm.auth_service.exception;

public class AuthOperationException extends RuntimeException {
    public AuthOperationException(String message) {
        super(message);
    }
}
