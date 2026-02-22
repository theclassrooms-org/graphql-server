package com.theclassrooms.graphqlserver.grpc.client;

import com.theclassrooms.graphqlserver.grpc.channel.GrpcStubFactory;
import com.theclassrooms.proto.classroom.*;
import com.theclassrooms.user.proto.GetUsersByIdsRequest;
import com.theclassrooms.user.proto.GetUsersByIdsResponse;
import com.theclassrooms.user.proto.UserServiceGrpc;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserGrpcClient {
    private final GrpcStubFactory grpcStubFactory;

    @Retry(name = "classroomGrpc")
    public GetUsersByIdsResponse getUsersByIds(List<UUID> ids) {
        log.info("getUsersByIds({})", ids);
        UserServiceGrpc.UserServiceBlockingStub stub = grpcStubFactory.getUserStub();

        GetUsersByIdsRequest request = GetUsersByIdsRequest.newBuilder()
                .addAllIds(
                        ids.stream()
                                .map(UUID::toString)
                                .toList()
                )
                .build();
        return stub.getUsersByIds(request);
    }
}
