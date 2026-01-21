package com.theclassrooms.graphqlserver.grpc.client;

import com.theclassrooms.graphqlserver.grpc.channel.GrpcStubFactory;
import com.theclassrooms.proto.classroom.ClassroomServiceGrpc;
import com.theclassrooms.proto.classroom.GetClassroomRequest;
import com.theclassrooms.proto.classroom.GetClassroomResponse;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassroomGrpcClient {
    private final GrpcStubFactory grpcStubFactory;

    @Retry(name = "classroomGrpc")
    public GetClassroomResponse getClassroom(String classroomId) {
        log.info("getClassroom({})", classroomId);
        ClassroomServiceGrpc.ClassroomServiceBlockingStub stub = grpcStubFactory.getClassroomStub();

        GetClassroomRequest request = GetClassroomRequest.newBuilder()
                .setId(classroomId)
                .build();

        return stub.getClassroom(request);
    }
}
