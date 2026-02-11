package com.theclassrooms.graphqlserver.resolver;

import com.theclassrooms.graphqlserver.dto.ClassroomDto;
import com.theclassrooms.graphqlserver.dto.PageDto;
import com.theclassrooms.graphqlserver.dto.PageInfoDto;
import com.theclassrooms.graphqlserver.dto.PageableInputDto;
import com.theclassrooms.graphqlserver.grpc.client.ClassroomGrpcClient;
import com.theclassrooms.graphqlserver.mapper.ClassroomMapper;
import com.theclassrooms.graphqlserver.service.MockDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
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
        List<ClassroomDto> filteredClassrooms = mockDataService.findBy(
                ClassroomDto.class,
                instructorId,
                ClassroomDto::getInstructorId
        );

        int page = pageableInput.getPage();
        int size = pageableInput.getSize();

        long totalElements = filteredClassrooms.size();
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalElements / size);

        int start = size * page;
        int end = Math.min(start + size, filteredClassrooms.size());

        Comparator<ClassroomDto> comparator = buildComparator(pageableInput.getSorts());

        if (comparator != null) {
            filteredClassrooms = filteredClassrooms.stream()
                    .sorted(comparator)
                    .toList();
        }

        List<ClassroomDto> pageContent =
                start < filteredClassrooms.size()
                        ? filteredClassrooms.subList(start, end)
                        : List.of();

        PageInfoDto pageInfo = PageInfoDto.builder()
                .totalPages(totalPages)
                .totalElements(totalElements)
                .number(page)
                .size(size)
                .first(page == 1)
                .last(page >= totalPages)
                .empty(pageContent.isEmpty())
                .build();

        return PageDto.<ClassroomDto>builder()
                .content(pageContent)
                .pageInfo(pageInfo)
                .build();
    }

    private Comparator<ClassroomDto> buildComparator(List<String> sorts) {

        if (sorts == null || sorts.isEmpty()) {
            return null;
        }

        Comparator<ClassroomDto> comparator = null;

        for (String sort : sorts) {

            String[] parts = sort.split(",");
            if (parts.length != 2) continue;

            String field = parts[0];
            String direction = parts[1];

            Comparator<ClassroomDto> fieldComparator = switch (field) {
                case "name" -> Comparator.comparing(ClassroomDto::getName,
                        Comparator.nullsLast(String::compareToIgnoreCase));

                case "endTime" -> Comparator.comparing(ClassroomDto::getEndTime,
                        Comparator.nullsLast(Comparator.naturalOrder()));

                case "type" -> Comparator.comparing(ClassroomDto::getType);

                case "status" -> Comparator.comparing(ClassroomDto::getStatus);

                default -> null;
            };

            if (fieldComparator == null) continue;

            if ("desc".equalsIgnoreCase(direction)) {
                fieldComparator = fieldComparator.reversed();
            }

            comparator = (comparator == null)
                    ? fieldComparator
                    : comparator.thenComparing(fieldComparator);
        }

        return comparator;
    }
}
