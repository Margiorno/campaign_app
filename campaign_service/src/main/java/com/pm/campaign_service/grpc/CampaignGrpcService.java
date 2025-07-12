package com.pm.campaign_service.grpc;

import com.pm.campaign_service.exception.CampaignOperationException;
import com.pm.campaign_service.mapper.CampaignMapper;
import com.pm.proto.CampaignProto;
import com.pm.proto.CampaignServiceGrpc;
import com.pm.campaign_service.service.CampaignService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@GrpcService
public class CampaignGrpcService extends CampaignServiceGrpc.CampaignServiceImplBase {

    private final CampaignService campaignService;

    @Autowired
    public CampaignGrpcService(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @Override
    public void getCampaignById(CampaignProto.CampaignRequest request, StreamObserver<CampaignProto.CampaignResponse> responseObserver) {
        try {
            var id = UUID.fromString(request.getId());
            var responseDTO = campaignService.findById(id);
            var response = CampaignMapper.toProto(responseDTO);

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (CampaignOperationException e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid UUID format: " + e.getMessage())
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void createCampaign(CampaignProto.CampaignRequest request, StreamObserver<CampaignProto.CampaignResponse> responseObserver) {
        var requestDTO = CampaignMapper.fromProto(request);
        var responseDTO = campaignService.save(requestDTO);
        var response = CampaignMapper.toProto(responseDTO);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateCampaign(CampaignProto.CampaignRequest request, StreamObserver<CampaignProto.CampaignResponse> responseObserver) {
        var id = UUID.fromString(request.getId());
        var requestDTO = CampaignMapper.fromProto(request);
        var responseDTO = campaignService.update(requestDTO, id);
        var response = CampaignMapper.toProto(responseDTO);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteCampaign(CampaignProto.CampaignRequest request, StreamObserver<CampaignProto.CampaignResponse> responseObserver) {
        try {
            var id = UUID.fromString(request.getId());
            campaignService.delete(id);

            var response = CampaignProto.CampaignResponse.newBuilder().setId(id.toString()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (CampaignOperationException e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid UUID format: " + e.getMessage())
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error: " + e.getMessage())
                            .asRuntimeException()
            );
        }
    }
}
