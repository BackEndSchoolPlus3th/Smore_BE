package com.meossamos.smore.domain.article.recruitmentArticle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RecruitmentArticleResponseData {
    private Long id;
    private String title;
    private String introduction;
    private String thumbnailUrl;
    private Boolean isRecruiting;
    private String writerName;
    private String writerProfile;
    private Long clipCount;
    private String hashtagList;
}
