package com.meossamos.smore.domain.article.studyArticle.dto.request;

import com.meossamos.smore.domain.member.member.entity.Member;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyArticleCreateRequest {

    private String title;
    private String content;
    private Member member;
}