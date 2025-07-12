package com.pm.stats_service.grpc;

import com.pm.proto.StatsProto;
import com.pm.proto.StatsServiceGrpc;
import com.pm.stats_service.service.StatsService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@GrpcService
public class StatsGrpcService extends StatsServiceGrpc.StatsServiceImplBase {

    private final StatsService statsService;

    @Autowired
    public StatsGrpcService(StatsService statsService) {
        this.statsService = statsService;
    }

    @Override
    public void getStatsById(StatsProto.StatsRequest request, StreamObserver<StatsProto.StatsResponse> responseObserver) {
        var statsDto = statsService.findById(UUID.fromString(request.getId()));

        StatsProto.StatsResponse response = StatsProto.StatsResponse.newBuilder()
                .setId(statsDto.getId())
                .setClicks(Long.parseLong(statsDto.getClicks()))
                .setSpentAmount(Double.parseDouble(statsDto.getSpentAmount()))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createStats(StatsProto.StatsRequest request, StreamObserver<StatsProto.StatsResponse> responseObserver) {
        var statsDto = statsService.create(UUID.fromString(request.getId()));

        StatsProto.StatsResponse response = StatsProto.StatsResponse.newBuilder()
                .setId(statsDto.getId())
                .setClicks(Long.parseLong(statsDto.getClicks()))
                .setSpentAmount(Double.parseDouble(statsDto.getSpentAmount()))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void registerClick(StatsProto.StatsRequest request, StreamObserver<StatsProto.StatsResponse> responseObserver) {
        var statsDto = statsService.registerClick(UUID.fromString(request.getId()));
        StatsProto.StatsResponse response = StatsProto.StatsResponse.newBuilder()
                .setId(statsDto.getId())
                .setClicks(Long.parseLong(statsDto.getClicks()))
                .setSpentAmount(Double.parseDouble(statsDto.getSpentAmount()))
                .build();

        super.registerClick(request, responseObserver);
    }

    @Override
    public void deleteStats(StatsProto.StatsRequest request, StreamObserver<StatsProto.StatsResponse> responseObserver) {
        statsService.deleteById(UUID.fromString(request.getId()));

        super.deleteStats(request, responseObserver);
    }
}
