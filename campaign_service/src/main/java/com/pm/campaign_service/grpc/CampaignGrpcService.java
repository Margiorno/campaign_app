package com.pm.campaign_service.grpc;

import com.pm.campaign_service.dto.CampaignRequestDTO;
import com.pm.campaign_service.dto.CampaignResponseDTO;
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
            UUID id = UUID.fromString(request.getId());
            CampaignResponseDTO dto = campaignService.findById(id);
            CampaignProto.CampaignResponse response = CampaignMapper.toProto(dto);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException iae) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format: " + iae.getMessage())
                    .asRuntimeException());
        } catch (CampaignOperationException coe) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(coe.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void createCampaign(CampaignProto.CampaignRequest request, StreamObserver<CampaignProto.CampaignResponse> responseObserver) {
        try {
            CampaignRequestDTO dto = CampaignMapper.fromProto(request);
            CampaignResponseDTO result = campaignService.save(dto);
            CampaignProto.CampaignResponse response = CampaignMapper.toProto(result);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (CampaignOperationException coe) {
            responseObserver.onError(Status.FAILED_PRECONDITION
                    .withDescription(coe.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void updateCampaign(CampaignProto.CampaignRequest request, StreamObserver<CampaignProto.CampaignResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getId());
            CampaignRequestDTO dto = CampaignMapper.fromProto(request);
            CampaignResponseDTO result = campaignService.update(dto, id);
            CampaignProto.CampaignResponse response = CampaignMapper.toProto(result);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException iae) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format: " + iae.getMessage())
                    .asRuntimeException());
        } catch (CampaignOperationException coe) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(coe.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    @Override
    public void deleteCampaign(CampaignProto.CampaignRequest request, StreamObserver<CampaignProto.CampaignResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getId());
            campaignService.delete(id);
            CampaignProto.CampaignResponse response = CampaignProto.CampaignResponse.newBuilder()
                    .setId(id.toString())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException iae) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format: " + iae.getMessage())
                    .asRuntimeException());
        } catch (CampaignOperationException coe) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(coe.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .asRuntimeException());
        }
    }


    @Override
    public void stopCampaign(CampaignProto.CampaignRequest request, StreamObserver<CampaignProto.CampaignResponse> responseObserver) {
        try {
            UUID id = UUID.fromString(request.getId());
            CampaignResponseDTO result = campaignService.stop(id);
            CampaignProto.CampaignResponse response = CampaignMapper.toProto(result);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException iae) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format: " + iae.getMessage())
                    .asRuntimeException());
        } catch (CampaignOperationException coe) {
            responseObserver.onError(Status.FAILED_PRECONDITION
                    .withDescription(coe.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .asRuntimeException());
        }
    }
}
