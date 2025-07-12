package com.pm.campaign_service.client;

import com.pm.proto.StatsProto;
import com.pm.proto.StatsServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class StatsGrpcClient {

    @GrpcClient("stats-service")
    private StatsServiceGrpc.StatsServiceBlockingStub statsStub;

    public StatsProto.StatsResponse getStatsById(String id) {
        var request = StatsProto.StatsRequest.newBuilder()
                .setId(id)
                .build();
        return statsStub.getStatsById(request);
    }

    public StatsProto.StatsResponse createStats(String id) {
        var request = StatsProto.StatsRequest.newBuilder()
                .setId(id)
                .build();
        return statsStub.createStats(request);
    }

    public StatsProto.StatsResponse registerClick(String id) {
        var request = StatsProto.StatsRequest.newBuilder()
                .setId(id)
                .build();
        return statsStub.registerClick(request);
    }

    public StatsProto.StatsResponse deleteStats(String id) {
        var request = StatsProto.StatsRequest.newBuilder()
                .setId(id)
                .build();
        return statsStub.deleteStats(request);
    }
}

