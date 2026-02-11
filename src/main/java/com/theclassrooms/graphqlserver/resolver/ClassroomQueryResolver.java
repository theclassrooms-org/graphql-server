package com.theclassrooms.graphqlserver.resolver;

import com.theclassrooms.graphqlserver.dto.ClassroomDto;
import com.theclassrooms.graphqlserver.dto.PageDto;
import com.theclassrooms.graphqlserver.dto.PageableInputDto;
import com.theclassrooms.graphqlserver.grpc.client.ClassroomGrpcClient;
import com.theclassrooms.graphqlserver.mapper.ClassroomMapper;
import com.theclassrooms.graphqlserver.service.MockDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ClassroomQueryResolver {

    private final MockDataService mockDataService;
    private final ClassroomGrpcClient classroomGrpcClient;
    private final ClassroomMapper classroomMapper;

    @QueryMapping
    public ClassroomDto getClassroomById(@Argument("id") UUID id) {
        return classroomMapper.toClassroomDTO(classroomGrpcClient.getClassroom(id.toString()).getClassroom());
    }

    @QueryMapping
    public List<ClassroomDto> getClassroomsByInstructor(@Argument("instructorId") UUID instructorId) {
        return mockDataService.findBy(
                ClassroomDto.class,
                instructorId,
                ClassroomDto::getInstructorId
        );
    }

    @QueryMapping
    public List<ClassroomDto> getAllClassrooms() {
        return mockDataService.getAll(ClassroomDto.class);
    }

    @QueryMapping
    public PageDto<ClassroomDto> getClassroomPageByInstructorId(
            @Argument("instructorId") UUID instructorId,
            @Argument("pageable") PageableInputDto pageableInput) {
        return classroomMapper.toPageDto(classroomGrpcClient
                .getClassroomPageByInstructorId(instructorId.toString(), pageableInput));
    }
}
