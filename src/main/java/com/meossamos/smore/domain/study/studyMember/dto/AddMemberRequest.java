package com.meossamos.smore.domain.study.studyMember.dto;

import com.meossamos.smore.domain.study.studyMember.entity.StudyPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AddMemberRequest {

    private Long memberId;
    private StudyPosition role;  // 'LEADER' 또는 'MEMBER'

    private Boolean recruitManage;
    private Boolean articleManage;
    private Boolean calendarManage;
    private Boolean settingManage;
}
