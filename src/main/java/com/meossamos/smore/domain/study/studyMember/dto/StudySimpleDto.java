package com.meossamos.smore.domain.study.studyMember.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StudySimpleDto {
    private Long id;
    private String title;
    private String introduction;
    private String thumbnailUrl;
    private String hashTags;
    private Integer memberCnt;
}