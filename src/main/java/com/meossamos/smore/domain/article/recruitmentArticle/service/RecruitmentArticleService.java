package com.meossamos.smore.domain.article.recruitmentArticle.service;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.repository.RecruitmentArticleRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.entity.Study;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecruitmentArticleService {
    private final RecruitmentArticleRepository recruitmentArticleRepository;

    public RecruitmentArticle saveRecruitmentArticle(String title, String content, @Nullable String region, @Nullable String imageUrls, LocalDateTime startDate, LocalDateTime endDate, Boolean isRecruiting, Integer maxMember, Member member, Study study) {
        RecruitmentArticle recruitmentArticle = RecruitmentArticle.builder()
                .title(title)
                .content(content)
                .region(region)
                .imageUrls(imageUrls)
                .startDate(startDate)
                .endDate(endDate)
                .isRecruiting(isRecruiting)
                .maxMember(maxMember)
                .member(member)
                .study(study)
                .build();

        return recruitmentArticleRepository.save(recruitmentArticle);
    }
}
