package com.theclassrooms.graphqlserver.grpc.channel.impl;

import com.theclassrooms.graphqlserver.exception.NoAvailableServiceInstanceException;
import com.theclassrooms.graphqlserver.grpc.channel.GrpcChannelRegistry;
import com.theclassrooms.graphqlserver.grpc.channel.GrpcStubFactory;
import com.theclassrooms.proto.classroom.ClassroomServiceGrpc;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GrpcStubFactoryImpl implements GrpcStubFactory {

    private final GrpcChannelRegistry grpcChannelRegistry;
    private final LoadBalancerClient loadBalancerClient;

    @Override
    public ClassroomServiceGrpc.ClassroomServiceBlockingStub getClassroomStub() {
        ServiceInstance instance = getServiceInstance("classroom-service");
        ManagedChannel channel = grpcChannelRegistry.getChannel(instance);
        return ClassroomServiceGrpc.newBlockingStub(channel);
    }

    ServiceInstance getServiceInstance(String serviceName) {
        return Optional.ofNullable(loadBalancerClient.choose(serviceName))
                .orElseThrow(() -> new NoAvailableServiceInstanceException("No available service for " + serviceName));
    }
}
