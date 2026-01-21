package com.theclassrooms.graphqlserver.grpc.client;

import com.theclassrooms.proto.classroom.ClassroomServiceGrpc;
import com.theclassrooms.proto.classroom.GetClassroomRequest;
import com.theclassrooms.proto.classroom.GetClassroomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassroomGrpcClient {

    private final ClassroomServiceGrpc.ClassroomServiceBlockingStub stub;

    public GetClassroomResponse getClassroom(String classroomId) {
        GetClassroomRequest request = GetClassroomRequest.newBuilder()
                .setId(classroomId)
                .build();

        return stub.getClassroom(request);
    }
}
