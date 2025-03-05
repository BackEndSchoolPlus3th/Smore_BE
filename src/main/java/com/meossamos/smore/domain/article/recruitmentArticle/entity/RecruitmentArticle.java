package com.meossamos.smore.domain.article.recruitmentArticle.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meossamos.smore.domain.article.recruitmentArticleClip.entity.RecruitmentArticleClip;
import com.meossamos.smore.domain.article.recruitmentArticleComment.entity.RecruitmentArticleComment;
import com.meossamos.smore.domain.article.recruitmentArticleHashTag.entity.RecruitmentArticleHashTag;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RecruitmentArticle extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String introduction;

    @Column(nullable = true)
    private String region;

    @Column(nullable = true)
    private String thumbnailUrl;

    @Column(nullable = true)
    private String imageUrls;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean isRecruiting;

    @Column(nullable = false)
    private Integer maxMember;

    @Column(nullable = true)
    private String hashTags;

    @Column(nullable = false)
    private Integer clipCount;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Study study;

    @JsonIgnore
    @OneToMany(mappedBy = "recruitmentArticle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<RecruitmentArticleHashTag> recruitmentArticleHashTagList = new ArrayList<>();


    @JsonIgnore
    @OneToMany(mappedBy = "recruitmentArticle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<RecruitmentArticleClip> recruitmentArticleClipList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "recruitmentArticle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<RecruitmentArticleComment> recruitmentArticleCommentList = new ArrayList<>();
}
