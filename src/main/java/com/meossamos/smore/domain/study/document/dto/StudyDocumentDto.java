package com.meossamos.smore.domain.study.document.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyDocumentDto {
    private String name;
    private String url;
}