package com.theclassrooms.graphqlserver.mapper;

import com.google.protobuf.Timestamp;
import com.theclassrooms.graphqlserver.dto.ClassroomDTO;
import org.mapstruct.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

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
    ClassroomDTO toClassroomDTO(
            com.theclassrooms.proto.classroom.Classroom classroom
    );

    default OffsetDateTime map(Timestamp time) {
        if (time == null) return null;
        return OffsetDateTime.ofInstant(
                Instant.ofEpochSecond(time.getSeconds(), time.getNanos()),
                ZoneOffset.UTC
        );
    }
}
