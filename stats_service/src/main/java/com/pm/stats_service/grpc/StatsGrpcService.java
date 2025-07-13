package com.pm.stats_service.grpc;

import com.pm.proto.StatsProto;
import com.pm.proto.StatsServiceGrpc;
import com.pm.stats_service.exception.GrpcRequestException;
import com.pm.stats_service.exception.StatsOperationException;
import com.pm.stats_service.service.StatsService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@GrpcService
public class StatsGrpcService extends StatsServiceGrpc.StatsServiceImplBase {

    private final StatsService statsService;
    private static final Logger logger = LoggerFactory.getLogger(StatsGrpcService.class);

    @Autowired
    public StatsGrpcService(StatsService statsService) {
        this.statsService = statsService;
    }

    @Override
    public void getStatsById(StatsProto.StatsRequest request, StreamObserver<StatsProto.StatsResponse> responseObserver) {
        try {
            var statsDto = statsService.findById(UUID.fromString(request.getId()));
            StatsProto.StatsResponse response = StatsProto.StatsResponse.newBuilder()
                    .setId(statsDto.getId())
                    .setClicks(statsDto.getClicks())
                    .setSpentAmount(statsDto.getSpentAmount())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (StatsOperationException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Internal error").withCause(e).asRuntimeException());
        }
    }

    @Override
    public void createStats(StatsProto.StatsRequest request, StreamObserver<StatsProto.StatsResponse> responseObserver) {
        try {
            var statsDto = statsService.create(UUID.fromString(request.getId()));
            StatsProto.StatsResponse response = StatsProto.StatsResponse.newBuilder()
                    .setId(statsDto.getId())
                    .setClicks(statsDto.getClicks())
                    .setSpentAmount(statsDto.getSpentAmount())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (StatsOperationException e) {
            responseObserver.onError(Status.FAILED_PRECONDITION.withDescription(e.getMessage()).asRuntimeException());
        } catch (GrpcRequestException e) {
            responseObserver.onError(Status.FAILED_PRECONDITION.withDescription("Dependency error: " + e.getMessage()).asRuntimeException());
        }catch (Exception e) {

            logger.error("Error creating stats for campaign {}", request.getId(), e);

            responseObserver.onError(Status.INTERNAL.withDescription("Internal error").withCause(e).asRuntimeException());
        }
    }

    @Override
    public void registerClick(StatsProto.StatsRequest request, StreamObserver<StatsProto.StatsResponse> responseObserver) {
        try {
            var statsDto = statsService.registerClick(UUID.fromString(request.getId()));
            StatsProto.StatsResponse response = StatsProto.StatsResponse.newBuilder()
                    .setId(statsDto.getId())
                    .setClicks(statsDto.getClicks())
                    .setSpentAmount(statsDto.getSpentAmount())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (StatsOperationException e) {
            responseObserver.onError(Status.FAILED_PRECONDITION.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Internal error").withCause(e).asRuntimeException());
        }
    }

    @Override
    public void deleteStats(StatsProto.StatsRequest request, StreamObserver<StatsProto.StatsResponse> responseObserver) {
        try {
            statsService.deleteById(UUID.fromString(request.getId()));

            StatsProto.StatsResponse response = StatsProto.StatsResponse.newBuilder()
                    .setId(request.getId())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (StatsOperationException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Internal error").withCause(e).asRuntimeException());
        }
    }
}
