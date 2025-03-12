package com.meossamos.smore.domain.study.studyMember.dto;

import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyMemberDto {
    private Long memberId;
    private String memberName;
    private Boolean permissionRecruitManage;
    private Boolean permissionArticleManage;
    private Boolean permissionCalendarManage;
    private Boolean permissionSettingManage;

    public static StudyMemberDto fromEntity(StudyMember studyMember) {
        return new StudyMemberDto(
                studyMember.getMember().getId(),
                studyMember.getMember().getNickname(),
                studyMember.getPermissionRecruitManage(),
                studyMember.getPermissionArticleManage(),
                studyMember.getPermissionCalendarManage(),
                studyMember.getPermissionSettingManage()
        );
    }
}
