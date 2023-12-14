package net.guzari.search.rest.controller;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import net.guzari.search.rest.grpc.UserFeatureServiceGrpc;
import net.guzari.search.rest.grpc.UserRequest;
import net.guzari.search.rest.grpc.UserResponse;

import java.util.Set;

@GrpcService
public class UserGrpcServiceTestImpl extends UserFeatureServiceGrpc.UserFeatureServiceImplBase {

    @Override
    public void findFeaturesByEmail(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        Set<String> featuresByEmail = Set.of("BASE", "SEARCH", "AUTOCOMPLETE");

        UserResponse userResponse = UserResponse.newBuilder()
                .addAllFeatures(featuresByEmail)
                .build();

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }
}
