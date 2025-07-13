package com.pm.campaign_service.util;

import com.pm.campaign_service.exception.GrpcRequestException;

public class GrpcExceptionUtil {
    public static GrpcRequestException mapToGrpcException(io.grpc.StatusRuntimeException e, String baseMessage) {
        return switch (e.getStatus().getCode()) {
            case NOT_FOUND -> new GrpcRequestException(baseMessage + ": not found.");
            case UNAVAILABLE -> new GrpcRequestException(baseMessage + ": service unavailable.");
            case DEADLINE_EXCEEDED -> new GrpcRequestException(baseMessage + ": request timed out.");
            default -> new GrpcRequestException(baseMessage + ": " + e.getMessage());
        };
    }
}
