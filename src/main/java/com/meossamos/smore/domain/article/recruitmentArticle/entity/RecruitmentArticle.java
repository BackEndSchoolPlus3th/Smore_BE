package com.meossamos.smore.domain.article.recruitmentArticle.entity;

import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private String region;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime updateDate;

    @Column(nullable = true)
    private String imageUrls;

    @Column(nullable = false)
    private LocalDateTime startDate;
}
