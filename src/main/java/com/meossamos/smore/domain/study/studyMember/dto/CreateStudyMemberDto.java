package com.meossamos.smore.domain.study.studyMember.dto;

import com.meossamos.smore.domain.study.studyMember.entity.StudyPosition;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CreateStudyMemberDto {
    String studyTitle;
    String nickname;
    String position;
    boolean permissionRecruitManage;
    boolean permissionArticleManage;
    boolean permissionCalendarManage;
    boolean permissionSettingManage;
}
