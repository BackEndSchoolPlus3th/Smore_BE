package com.meossamos.smore.domain.article.recruitmentArticle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRecruitmentArticleDto {
    private String title;
    private String content;
    private String introduction;
    private String region;
    private String thumbnailUrl;
    private List<String> imageUrls;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isRecruiting;
    private Integer maxMember;
    private List<String> hashTags;
}