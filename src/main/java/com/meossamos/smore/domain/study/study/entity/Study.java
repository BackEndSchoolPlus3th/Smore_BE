package com.meossamos.smore.domain.study.study.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import com.meossamos.smore.domain.chat.groupChat.entity.GroupChatRoom;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.document.entity.StudyDocument;
import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Study extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer memberCnt;

    @Column(nullable = true)
    private String imageUrls;

    @Column(nullable = true)
    private String introduction;

    @Column(nullable = true)
    private String hashTags;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member leader;

    @JsonIgnore
    @OneToMany(mappedBy = "studyDocument", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<StudyDocument> studyDocumentList = new ArrayList<>();

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
    @OneToMany(mappedBy = "studyArticle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<StudyArticle> studyArticleList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "studySchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<StudySchedule> studyScheduleList = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "groupChatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private GroupChatRoom groupChatRoom;
}
