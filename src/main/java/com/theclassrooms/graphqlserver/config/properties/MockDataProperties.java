package com.theclassrooms.graphqlserver.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "mock-data")
public class MockDataProperties {
    private List<MockDataMapping> mappings;

    @Data
    public static class MockDataMapping {
        private String filename;
        private String type;  // "list" or "single"
        private String className;  // Full class name
    }
}
