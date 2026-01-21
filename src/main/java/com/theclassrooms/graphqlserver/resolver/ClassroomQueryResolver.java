package com.theclassrooms.graphqlserver.resolver;

import com.theclassrooms.graphqlserver.dto.ClassroomDTO;
import com.theclassrooms.graphqlserver.enums.ClassroomStatus;
import com.theclassrooms.graphqlserver.enums.ClassroomType;
import com.theclassrooms.graphqlserver.grpc.client.ClassroomGrpcClient;
import com.theclassrooms.graphqlserver.mapper.ClassroomMapper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class ClassroomQueryResolver {

    private final List<ClassroomDTO> classrooms;
    private final ClassroomGrpcClient classroomGrpcClient;
    private final ClassroomMapper classroomMapper;

    public ClassroomQueryResolver(ClassroomGrpcClient classroomGrpcClient, ClassroomMapper classroomMapper) {
        this.classroomGrpcClient = classroomGrpcClient;
        this.classroomMapper = classroomMapper;
        this.classrooms = List.of(
                ClassroomDTO.builder()
                        .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                        .name("Spring Boot Fundamentals")
                        .description("Learn Spring Boot from scratch")
                        .type(ClassroomType.PUBLIC)
                        .status(ClassroomStatus.ACTIVE)
                        .instructorId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                        .classCode("SPRING-101")
                        .build(),
                ClassroomDTO.builder()
                        .id(UUID.fromString("21111111-1111-1111-1111-111111111112"))
                        .name("Java Fundamentals")
                        .description("Learn Java")
                        .type(ClassroomType.PRIVATE)
                        .status(ClassroomStatus.ACTIVE)
                        .instructorId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                        .classCode("JAVA-202")
                        .build()
        );
    }

    @QueryMapping
    public ClassroomDTO getClassroomById(@Argument("id") UUID id) {
        return classroomMapper.toClassroomDTO(classroomGrpcClient.getClassroom(id.toString()).getClassroom());
    }

    @QueryMapping
    public List<ClassroomDTO> getClassroomsByInstructor(@Argument("instructorId") UUID instructorId) {
        return classrooms.stream()
                .filter(c -> instructorId.equals(c.getInstructorId()))
                .toList();
    }

    @QueryMapping
    public List<ClassroomDTO> getAllClassrooms() {
        return classrooms;
    }
}
