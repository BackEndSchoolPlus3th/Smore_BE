package com.meossamos.smore.domain.article.studyArticle.entity;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StudyArticle extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private String imageUrls;

    @ElementCollection
    @Column(nullable = true)
    private List<String> attachments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
