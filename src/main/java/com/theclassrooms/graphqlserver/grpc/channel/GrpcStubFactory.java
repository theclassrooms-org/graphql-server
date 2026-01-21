package com.theclassrooms.graphqlserver.grpc.channel;

import com.theclassrooms.proto.classroom.ClassroomServiceGrpc;

public interface GrpcStubFactory {

    ClassroomServiceGrpc.ClassroomServiceBlockingStub getClassroomStub();
}
