package com.theclassrooms.graphqlserver.dto;

import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<T> {
    private List<T> content;
    private PageInfoDto pageInfo;
}
