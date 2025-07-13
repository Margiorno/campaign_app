package com.pm.stats_service.util;


import com.pm.stats_service.exception.GrpcRequestException;

public class GrpcExceptionUtil {
    public static GrpcRequestException mapToGrpcException(io.grpc.StatusRuntimeException e, String baseMessage) {
        return switch (e.getStatus().getCode()) {
            case INVALID_ARGUMENT -> new GrpcRequestException(baseMessage + ": invalid argument. " + e.getMessage());
            case NOT_FOUND -> new GrpcRequestException(baseMessage + ": not found. " + e.getMessage());
            case FAILED_PRECONDITION -> new GrpcRequestException(baseMessage + ": operation not allowed. " + e.getMessage());
            case UNAVAILABLE -> new GrpcRequestException(baseMessage + ": service unavailable.");
            case DEADLINE_EXCEEDED -> new GrpcRequestException(baseMessage + ": request timed out.");
            case INTERNAL -> new GrpcRequestException(baseMessage + ": internal server error. " + e.getMessage());
            default -> new GrpcRequestException(baseMessage + ": unexpected error. " + e.getMessage());
        };
    }
}
