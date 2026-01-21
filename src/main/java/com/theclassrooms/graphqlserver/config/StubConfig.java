package com.theclassrooms.graphqlserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ImportGrpcClients;

@Configuration
@ImportGrpcClients(target = "localhost:9090", basePackages = "com.theclassrooms.proto")
public class StubConfig {
}
