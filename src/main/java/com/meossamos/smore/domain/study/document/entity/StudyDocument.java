package com.meossamos.smore.domain.study.document.entity;

import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
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
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StudyDocument extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    private StudyArticle studyArticle;
}
