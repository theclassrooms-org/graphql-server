package com.theclassrooms.graphqlserver.grpc.client;

import com.theclassrooms.graphqlserver.dto.PageableInputDto;
import com.theclassrooms.graphqlserver.grpc.channel.GrpcStubFactory;
import com.theclassrooms.proto.classroom.*;
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

    @Retry(name = "classroomGrpc")
    public GetClassroomPageByInstructorIdResponse getClassroomPageByInstructorId(String instructorId, PageableInputDto pageableInput) {
        ClassroomServiceGrpc.ClassroomServiceBlockingStub stub = grpcStubFactory.getClassroomStub();

        PageableRequest.Builder pageableBuilder = PageableRequest.newBuilder()
                .setPage(pageableInput.getPage())
                .setSize(pageableInput.getSize());

        if (pageableInput.getSorts() != null && !pageableInput.getSorts().isEmpty()) {
            pageableBuilder.addAllSorts(pageableInput.getSorts());
        }

        GetClassroomPageByInstructorIdRequest request = GetClassroomPageByInstructorIdRequest.newBuilder()
                .setInstructorId(instructorId)
                .setPageable(pageableBuilder.build())
                .build();

        return stub.getClassroomPageByInstructorId(request);
    }
}
