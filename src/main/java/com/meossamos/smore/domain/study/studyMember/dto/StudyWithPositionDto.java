package com.meossamos.smore.domain.study.studyMember.dto;

import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.studyMember.entity.StudyPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
@SuperBuilder
public class StudyWithPositionDto {
    private Study study;
    private StudyPosition position;
    private LocalDateTime createdDate;
}
