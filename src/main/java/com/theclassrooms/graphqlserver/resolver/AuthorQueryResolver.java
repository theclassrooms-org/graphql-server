package com.theclassrooms.graphqlserver.resolver;

import com.theclassrooms.graphqlserver.dto.ClassroomDto;
import com.theclassrooms.graphqlserver.dto.InstructorDto;
import com.theclassrooms.graphqlserver.service.MockDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Slf4j
public class AuthorQueryResolver {

    private final MockDataService mockDataService;

    @BatchMapping(typeName = "Classroom", field = "instructor")
    public CompletableFuture<Map<ClassroomDto, InstructorDto>> instructor(List<ClassroomDto> classrooms) {

        List<UUID> instructorIds = classrooms.stream()
                .map(ClassroomDto::getInstructorId)
                .distinct()
                .toList();
        log.info("Batch loading instructors: {}", instructorIds);

        Map<UUID, InstructorDto> instructorsMap = mockDataService.findMapByIds(InstructorDto.class, instructorIds, InstructorDto::getId);

        return CompletableFuture.completedFuture(
                classrooms.stream()
                        .collect(Collectors.toMap(
                                classroom -> classroom,
                                classroom -> instructorsMap.computeIfAbsent(classroom.getInstructorId(),
                                        (k) -> InstructorDto.builder()
                                                .id(k)
                                                .name("UNKNOWN")
                                                .avatar("UNKNOWN")
                                                .username("UNKNOWN")
                                                .build())
                        ))
        );
    }
}
