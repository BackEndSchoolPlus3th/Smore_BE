package com.meossamos.smore.domain.article.recruitmentArticleHashTag.entity;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class RecruitmentArticleHashTag extends BaseEntity {
    private String hashTag;

    @ManyToOne(fetch = FetchType.LAZY)
    private RecruitmentArticle recruitmentArticle;
}
