package com.meossamos.smore.domain.article.recruitmentArticleClip.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class RecruitmentArticleClipResponseDTO {
    private Long recruitmentArticleId;
    private String title;
    private String introduction;
    private Boolean isRecruiting;
    private String hashTags;
}