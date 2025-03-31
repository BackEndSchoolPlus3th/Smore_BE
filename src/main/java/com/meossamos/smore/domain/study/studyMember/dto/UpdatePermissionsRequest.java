package com.meossamos.smore.domain.study.studyMember.dto;

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

public class UpdatePermissionsRequest {

    private Long memberId;

    private Boolean recruitManage;
    private Boolean articleManage;
    private Boolean calendarManage;
    private Boolean settingManage;
}
