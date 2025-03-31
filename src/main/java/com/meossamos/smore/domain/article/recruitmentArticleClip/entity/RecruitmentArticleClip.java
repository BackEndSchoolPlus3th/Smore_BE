package com.meossamos.smore.domain.article.recruitmentArticleClip.entity;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.global.jpa.BaseEntity;
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
public class RecruitmentArticleClip extends BaseEntity {
    @ManyToOne
    private RecruitmentArticle recruitmentArticle;

    @ManyToOne
    private Member member;
}
