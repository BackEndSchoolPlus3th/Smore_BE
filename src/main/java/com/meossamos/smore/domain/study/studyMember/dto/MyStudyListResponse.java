package com.meossamos.smore.domain.study.studyMember.dto;

import com.meossamos.smore.domain.study.studyMember.entity.StudyPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MyStudyListResponse {
    private Long id;
    private String title;
    private String introduction;
    private String thumbnailUrl;
    private StudyPosition studyPosition;
    private String hashTags;
    private Integer memberCnt;
    private LocalDate registrationDate;
}
