package com.meossamos.smore.domain.article.recruitmentArticle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SimpleRecruitmentResponse {
    private List<SimpleRecruitmentDto> recruitments;
    private boolean isPermission;
}
