package com.meossamos.smore.domain.article.recruitmentArticle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RecruitmentArticleResponseData {
    private Long id;
    private String title;
    private String introduction;
    private String region;
    private String imageUrl;
    private Boolean isRecruiting;
    private String writerName;
    private String writerProfileImageUrl;
    private Long ClipCount;
    private String hashTags;
}
