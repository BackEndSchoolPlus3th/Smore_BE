package com.meossamos.smore.domain.article.recruitmentArticleComment.entity;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RecruitmentArticleComment extends BaseEntity {
    @Column(nullable = false)
    private String comment;

    @ManyToOne
    private RecruitmentArticle recruitmentArticle;

    @ManyToOne
    private Member member;
}
