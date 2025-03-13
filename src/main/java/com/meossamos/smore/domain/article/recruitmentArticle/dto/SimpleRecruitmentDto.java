package com.meossamos.smore.domain.article.recruitmentArticle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SimpleRecruitmentDto {
    private Long id;
    private String title;
    private String introduction;
    private String thumbnailUrl;
    private Boolean isRecruiting;
    private String writerName;
    private String writerProfile;
    private Integer clipCount;
    private String hashTags;
}
