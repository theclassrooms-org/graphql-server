package com.theclassrooms.graphqlserver.resolver;

import com.theclassrooms.graphqlserver.dto.ClassroomDTO;
import com.theclassrooms.graphqlserver.dto.InstructorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class AuthorQueryResolver {

    @BatchMapping(typeName = "Classroom", field = "instructor")
    public CompletableFuture<Map<ClassroomDTO, InstructorDTO>> instructor(List<ClassroomDTO> classrooms) {

        List<UUID> instructorIds = classrooms.stream()
                .map(ClassroomDTO::getInstructorId)
                .distinct()
                .toList();

        log.info("Batch loading instructors: {}", instructorIds);

        Map<UUID, InstructorDTO> instructorMap = instructorIds.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> InstructorDTO.builder()
                                .id(id)
                                .name("Hudson")
                                .avatar("https://fifaaddict.com/fo3img/players/ogimage/p158023.jpg?20170901")
                                .build()
                ));

        return CompletableFuture.completedFuture(
                classrooms.stream()
                        .collect(Collectors.toMap(
                                classroom -> classroom,
                                classroom -> instructorMap.get(classroom.getInstructorId())
                        ))
        );
    }
}
