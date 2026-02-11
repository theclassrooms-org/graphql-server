package com.theclassrooms.graphqlserver.mapper;

import com.google.protobuf.Timestamp;
import com.theclassrooms.graphqlserver.dto.ClassroomDto;
import com.theclassrooms.graphqlserver.dto.PageDto;
import com.theclassrooms.graphqlserver.dto.PageInfoDto;
import com.theclassrooms.proto.classroom.GetClassroomPageByInstructorIdResponse;
import org.mapstruct.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ClassroomMapper {

    @Mapping(
            target = "status",
            expression = "java(ProtoEnumMapper.mapEnum(classroom.getStatus(), com.theclassrooms.graphqlserver.enums.ClassroomStatus.class))"
    )
    @Mapping(
            target = "type",
            expression = "java(ProtoEnumMapper.mapEnum(classroom.getType(), com.theclassrooms.graphqlserver.enums.ClassroomType.class))"
    )
    ClassroomDto toClassroomDTO(
            com.theclassrooms.proto.classroom.Classroom classroom
    );

    default OffsetDateTime map(Timestamp time) {
        if (time == null) return null;
        return OffsetDateTime.ofInstant(
                Instant.ofEpochSecond(time.getSeconds(), time.getNanos()),
                ZoneOffset.UTC
        );
    }

    default PageDto<ClassroomDto> toPageDto(
            GetClassroomPageByInstructorIdResponse response
    ) {
        if (response == null || !response.hasPage()) {
            return PageDto.<ClassroomDto>builder()
                    .content(List.of())
                    .build();
        }

        var protoPage = response.getPage();

        List<ClassroomDto> content = protoPage.getContentList()
                .stream()
                .map(this::toClassroomDTO)
                .toList();

        var protoPageInfo = protoPage.getPageInfo();

        PageInfoDto pageInfo = PageInfoDto.builder()
                .totalElements(protoPageInfo.getTotalElements())
                .totalPages(protoPageInfo.getTotalPages())
                .number(protoPageInfo.getNumber())
                .size(protoPageInfo.getSize())
                .first(protoPageInfo.getFirst())
                .last(protoPageInfo.getLast())
                .empty(protoPageInfo.getEmpty())
                .build();

        return PageDto.<ClassroomDto>builder()
                .content(content)
                .pageInfo(pageInfo)
                .build();
    }
}
