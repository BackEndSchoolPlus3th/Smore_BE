package com.meossamos.smore.domain.study.studyMember.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    @JsonIgnore
    private Study study;

    @Column(nullable = false)
    private StudyPosition position; // LEADER,  MEMBER

    // 모집글 작성 권한
    @Column(nullable = false)
    private Boolean permissionRecruitManage;

    // 스터디 게시글 작성 권한
    @Column(nullable = false)
    private Boolean permissionArticleManage;

    // 스터디 일정 작성 권한
    @Column(nullable = false)
    private Boolean permissionCalendarManage;

    // 스터디 멤버 관리 권한
    @Column(nullable = false)
    private Boolean permissionSettingManage;
}
