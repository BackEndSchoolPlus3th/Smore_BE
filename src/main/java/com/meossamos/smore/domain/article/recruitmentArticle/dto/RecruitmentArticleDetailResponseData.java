package com.meossamos.smore.domain.article.recruitmentArticle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RecruitmentArticleDetailResponseData {
    private Long id;
    private Long studyId;
    private String title;
    private String content;
    private String introduction;
    private String region;
    private String imageUrls;
    private String startDate;
    private String endDate;
    private Boolean isRecruiting;
    private Integer maxMember;
    private String hashTags;
    private Integer clipCount;
    private boolean isPermission;
    private String writerName;
    private String writerProfileImageUrl;
    private LocalDateTime createdDate;
    private boolean isClipped;
}
