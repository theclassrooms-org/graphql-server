package com.theclassrooms.graphqlserver.service;

import com.theclassrooms.graphqlserver.config.properties.MockDataProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.type.CollectionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MockDataService {
    private final ObjectMapper objectMapper;
    private final MockDataProperties mockDataProperties;
    private final Map<Class<?>, Object> dataStore = new HashMap<>();

    public MockDataService(ObjectMapper objectMapper, MockDataProperties mockDataProperties) {
        this.objectMapper = objectMapper;
        this.mockDataProperties = mockDataProperties;
    }

    @PostConstruct
    public void init() {
        List<MockDataProperties.MockDataMapping> mappings = mockDataProperties.getMappings();
        if (mappings == null || mappings.isEmpty()) {
            log.warn("⚠️ No mock data mappings configured");
            return;
        }
        for (var mapping : mappings) {
            try {
                loadMapping(mapping);
            } catch (Exception e) {
                log.error("❌ Failed to load {}", mapping.getFilename(), e);
            }
        }
        log.info("✅ Mock data loaded: {} types", dataStore.size());
    }

    private void loadMapping(MockDataProperties.MockDataMapping mapping) throws Exception {
        final String fileName = mapping.getFilename();
        ClassPathResource resource = new ClassPathResource("mock-data/" + fileName);
        Class<?> clazz = Class.forName(mapping.getClassName());
        switch (mapping.getType()) {
            case "list" -> loadListData(resource, fileName, clazz);
            case "object" -> loadObjectData(resource, fileName, clazz);
            default -> throw new IllegalArgumentException(
                    "Unknown mapping type: " + mapping.getType());
        }
    }

    private void loadObjectData(ClassPathResource resource, String fileName, Class<?> clazz) throws Exception {
        var data = objectMapper.readValue(resource.getInputStream(), clazz);
        dataStore.put(clazz, data);
        log.info("📦 Loaded single object from {}", fileName);
    }

    private void loadListData(ClassPathResource resource, String fileName, Class<?> clazz) throws Exception {
        CollectionType listType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, clazz);
        List<?> data = objectMapper.readValue(resource.getInputStream(), listType);
        dataStore.put(clazz, data);
        log.info("📦 Loaded {} records from {}", data.size(), fileName);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAll(Class<T> clazz) {
        Object data = dataStore.get(clazz);
        if (data instanceof List<?>) {
            return (List<T>) data;
        }
        return List.of();
    }

    @SuppressWarnings("unchecked")
    public <T> T getSingle(Class<T> clazz) {
        Object data = dataStore.get(clazz);
        if (data != null && !(data instanceof List<?>)) {
            return (T) data;
        }
        return null;
    }

    public <T, ID> T findById(Class<T> clazz, ID id, Function<T, ID> idExtractor) {
        return getAll(clazz).stream()
                .filter(item -> idExtractor.apply(item).equals(id))
                .findFirst()
                .orElse(null);
    }

    public <T, ID> List<T> findBy(Class<T> clazz, ID value, Function<T, ID> fieldExtractor) {
        return getAll(clazz).stream()
                .filter(item -> fieldExtractor.apply(item).equals(value))
                .toList();
    }

    public <T, ID> Map<ID, T> findMapByIds(Class<T> clazz, List<ID> ids, Function<T, ID> idExtractor) {
        return getAll(clazz).stream()
                .filter(item -> ids.contains(idExtractor.apply(item)))
                .collect(Collectors.toMap(idExtractor, Function.identity()));
    }
}
