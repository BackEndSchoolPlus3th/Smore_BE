package com.meossamos.smore.domain.article.recruitmentArticleComment.dto;

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
public class RecruitmentArticleDetailCommentWithProfileResponseData  {
    private Long id;
    private String comment;
    private Long writerId;
    private String writerName;
    private String writerProfileImageUrl;
    private LocalDateTime createdDate;
    private Long publisherId;
}