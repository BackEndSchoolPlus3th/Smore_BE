package com.meossamos.smore.domain.member.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meossamos.smore.domain.alarm.alarm.entity.Alarm;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import com.meossamos.smore.domain.chat.chat.entity.ChatRoom;
import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Member extends BaseEntity {

//    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true)
    private LocalDate birthdate;

    @Column(nullable = true)
    private String region;

    @Column(nullable = true)
    private String hashTags;

    @JsonIgnore
    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Study> studyList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<ChatRoom> chatRoomList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "alarm", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Alarm> alarmList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "studyArticle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<StudyArticle> studyArticleList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "recruitmentArticle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<RecruitmentArticle> recruitmentArticleList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "studyMember", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<StudyMember> studyMemberList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "studySchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<StudySchedule> studyScheduleList = new ArrayList<>();
}
