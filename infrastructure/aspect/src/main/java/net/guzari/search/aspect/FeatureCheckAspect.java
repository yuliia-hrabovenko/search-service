package net.guzari.search.aspect;

import com.google.protobuf.ProtocolStringList;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.guzari.search.domain.report.CheckFeatures;
import net.guzari.search.domain.report.UserContext;
import net.guzari.search.grpc.UserFeatureServiceGrpc;
import net.guzari.search.grpc.UserRequest;
import net.guzari.search.grpc.UserResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class FeatureCheckAspect {

    @GrpcClient("grpc-server")
    private UserFeatureServiceGrpc.UserFeatureServiceBlockingStub userFeatureServiceBlockingStub;

    private final UserContext userContext;

    @Around("@annotation(checkFeatures)")
    public Object checkAccess(ProceedingJoinPoint joinPoint, CheckFeatures checkFeatures) throws Throwable {
        UserResponse response = userFeatureServiceBlockingStub.findFeaturesByEmail(
                UserRequest.newBuilder()
                        .setEmail(userContext.getAuthenticatedUserEmail())
                        .build());

        ProtocolStringList featuresList = response.getFeaturesList();
        String feature = checkFeatures.value().toString();
        if (!featuresList.contains(feature)) {
            throw new IllegalArgumentException(String.format("%s feature is not available", feature));
        }

        return joinPoint.proceed();
    }
}
