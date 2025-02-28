package com.meossamos.smore.domain.article.studyArticle.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyArticleCreateRequest {

    private String title;
    private String content;
    private String imageUrls;
    private List<String> attachments;
    private String hashTags;
}