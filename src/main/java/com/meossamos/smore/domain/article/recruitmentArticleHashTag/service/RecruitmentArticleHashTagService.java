package com.meossamos.smore.domain.article.recruitmentArticleHashTag.service;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticleHashTag.entity.RecruitmentArticleHashTag;
import com.meossamos.smore.domain.article.recruitmentArticleHashTag.repository.RecruitmentArticleHashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentArticleHashTagService {
    private final RecruitmentArticleHashTagRepository recruitmentArticleHashTagRepository;

    public RecruitmentArticleHashTag saveRecruitmentArticleHashTag(String hashTag, RecruitmentArticle recruitmentArticle) {
        RecruitmentArticleHashTag recruitmentArticleHashTag = RecruitmentArticleHashTag.builder()
                .hashTag(hashTag)
                .recruitmentArticle(recruitmentArticle)
                .build();

        return recruitmentArticleHashTagRepository.save(recruitmentArticleHashTag);
    }
}
