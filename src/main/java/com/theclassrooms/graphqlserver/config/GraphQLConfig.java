package com.theclassrooms.graphqlserver.config;

import graphql.scalars.ExtendedScalars;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.graphql.server.WebGraphQlInterceptor;

@Configuration
public class GraphQLConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder ->
                wiringBuilder.scalar(ExtendedScalars.GraphQLLong);
    }

    @Bean
    public WebGraphQlInterceptor webGraphQlInterceptor() {
        return (request, chain) -> {

            String authHeader = request.getHeaders().getFirst("Authorization");

            if (authHeader != null) {
                request.configureExecutionInput((executionInput, builder) ->
                        builder.graphQLContext(ctx ->
                                        ctx.put("Authorization", authHeader))
                                .build()
                );
            }

            return chain.next(request);
        };
    }
}
