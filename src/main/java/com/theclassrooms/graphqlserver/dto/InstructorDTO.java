package com.theclassrooms.graphqlserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class InstructorDTO {
    private UUID id;
    private String name;
    private String avatar;
}
