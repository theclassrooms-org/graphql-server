package com.theclassrooms.graphqlserver.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.theclassrooms.graphqlserver.dto.ClassroomDto;
import com.theclassrooms.graphqlserver.dto.InstructorDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class MockDataConfig {

    @Bean
    public Map<String, TypeReference<?>> mockDataTypes() {
        return Map.of(
                // List types
                "classrooms.json", new TypeReference<List<ClassroomDto>>() {},
                "instructors.json", new TypeReference<List<InstructorDto>>() {}

                // Single object types (example)
                // "app-config.json", new TypeReference<AppConfigDto>() {},
                // "statistics.json", new TypeReference<StatisticsDto>() {}
        );
    }
}
