package com.theclassrooms.graphqlserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GraphqlServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphqlServerApplication.class, args);
	}

}
