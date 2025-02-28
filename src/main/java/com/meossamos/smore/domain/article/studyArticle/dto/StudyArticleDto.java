package com.meossamos.smore.domain.article.studyArticle.dto;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.entity.Study;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyArticleDto {

    private Long id;
    private String title;
    private String content;
    private String imageUrls;
    private List<String> attachments;
    private String hashTags;
    private Study study;
    private Member member;
}
