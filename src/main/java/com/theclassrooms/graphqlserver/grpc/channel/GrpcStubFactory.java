package com.theclassrooms.graphqlserver.grpc.channel;

import com.theclassrooms.proto.classroom.ClassroomServiceGrpc;
import com.theclassrooms.user.proto.UserServiceGrpc;

public interface GrpcStubFactory {

    ClassroomServiceGrpc.ClassroomServiceBlockingStub getClassroomStub();
    UserServiceGrpc.UserServiceBlockingStub getUserStub();
}
