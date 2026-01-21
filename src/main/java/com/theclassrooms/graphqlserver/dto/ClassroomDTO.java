package com.theclassrooms.graphqlserver.dto;

import com.theclassrooms.graphqlserver.enums.ClassroomStatus;
import com.theclassrooms.graphqlserver.enums.ClassroomType;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class ClassroomDTO {
    private UUID id;
    private String name;
    private String description;
    private ClassroomType type;
    private ClassroomStatus status;
    private UUID instructorId;
    private String classCode;
    private OffsetDateTime endTime;
}
