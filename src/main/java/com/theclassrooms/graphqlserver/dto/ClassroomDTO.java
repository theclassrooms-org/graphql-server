package com.theclassrooms.graphqlserver.dto;

import com.theclassrooms.graphqlserver.enums.ClassroomStatus;
import com.theclassrooms.graphqlserver.enums.ClassroomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomDto {
    private UUID id;
    private String name;
    private String description;
    private ClassroomType type;
    private ClassroomStatus status;
    private UUID instructorId;
    private String classCode;
    private OffsetDateTime endTime;
    private String thumbnailUrl;
    private String bannerUrl;
}
