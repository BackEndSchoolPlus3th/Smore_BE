package com.meossamos.smore.domain.study.studyMember.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudyMemberPermissionDto {
    private Boolean permissionRecruitManage;
    private Boolean permissionArticleManage;
    private Boolean permissionCalendarManage;
    private Boolean permissionSettingManage;
}