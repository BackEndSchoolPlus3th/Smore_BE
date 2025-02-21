package com.meossamos.smore.domain.home.article.entity;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Article extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private String region;

    @Column(nullable = true)
    private String hashTags;

    @Column(nullable = true)
    private String imageUrls;

    @Column(nullable = true)
    private String attachments;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

}
