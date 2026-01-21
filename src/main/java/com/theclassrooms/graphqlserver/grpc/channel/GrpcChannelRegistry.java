package com.theclassrooms.graphqlserver.grpc.channel;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class GrpcChannelRegistry {
    private final Map<String, ManagedChannel> channels = new ConcurrentHashMap<>();

    public ManagedChannel getChannel(ServiceInstance instance) {
        final String key = instance.getInstanceId();

        return channels.computeIfAbsent(key, k -> {
            String grpcPort = Objects.requireNonNull(instance.getMetadata()).get("grpc-port");
            log.info("Create gRPC channel -> {}:{}", instance.getHost(), grpcPort);
            return ManagedChannelBuilder.forAddress(instance.getHost(), Integer.parseInt(grpcPort))
                    .usePlaintext()
                    .build();
        });
    }

    @PreDestroy
    public void shutdown() {
        channels.values().forEach(ManagedChannel::shutdown);
    }
}
