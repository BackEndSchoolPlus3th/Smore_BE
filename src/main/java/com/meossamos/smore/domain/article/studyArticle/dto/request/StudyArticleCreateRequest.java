package com.meossamos.smore.domain.article.studyArticle.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyArticleCreateRequest {

    private String title;
    private String content;
    private String imageUrls;
    private String attachments;
    private String hashTags;
}