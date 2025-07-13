package com.pm.stats_service.client;

import com.pm.proto.CampaignProto;
import com.pm.proto.CampaignServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class CampaignGrpcClient {

    @GrpcClient("campaign-service")
    private CampaignServiceGrpc.CampaignServiceBlockingStub campaignStub;

    public CampaignProto.CampaignResponse getCampaignById(String id) {
        var request = CampaignProto.CampaignRequest.newBuilder()
                .setId(id)
                .build();

        return campaignStub.getCampaignById(request);
    }

    public CampaignProto.CampaignResponse createCampaign(CampaignProto.CampaignRequest request) {
        return campaignStub.createCampaign(request);
    }

    public CampaignProto.CampaignResponse updateCampaign(CampaignProto.CampaignRequest request) {
        return campaignStub.updateCampaign(request);
    }

    public CampaignProto.CampaignResponse deleteCampaign(String id) {
        var request = CampaignProto.CampaignRequest.newBuilder()
                .setId(id)
                .build();

        return campaignStub.deleteCampaign(request);
    }

    public CampaignProto.CampaignResponse stopCampaign(String id) {
        var request = CampaignProto.CampaignRequest.newBuilder()
                .setId(id)
                .build();

        return campaignStub.stopCampaign(request);
    }
}