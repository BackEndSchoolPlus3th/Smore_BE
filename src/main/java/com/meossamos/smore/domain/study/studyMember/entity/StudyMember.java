package com.meossamos.smore.domain.study.studyMember.entity;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class StudyMember extends BaseEntity {

    @Column(nullable = false)
    private Boolean permissionRecruitManage;

    @Column(nullable = false)
    private Boolean permissionArticleManage;

    @Column(nullable = false)
    private Boolean permissionCalendarManage;

    @Column(nullable = false)
    private Boolean permissionSettingManage;

    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

}
