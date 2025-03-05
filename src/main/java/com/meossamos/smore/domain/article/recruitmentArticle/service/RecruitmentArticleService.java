package com.meossamos.smore.domain.article.recruitmentArticle.service;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.repository.RecruitmentArticleRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.global.util.HashTagUtil;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecruitmentArticleService {
    private final RecruitmentArticleRepository recruitmentArticleRepository;

    public RecruitmentArticle save(String title, String content, String introduction, @Nullable String region, @Nullable String thumbnailUrl, @Nullable String imageUrls, LocalDateTime startDate, LocalDateTime endDate, Boolean isRecruiting, Integer maxMember, String hashTags, Member member, Study study, Integer clipCount) {
        RecruitmentArticle recruitmentArticle = RecruitmentArticle.builder()
                .title(title)
                .content(content)
                .introduction(introduction)
                .region(region)
                .thumbnailUrl(thumbnailUrl)
                .imageUrls(imageUrls)
                .startDate(startDate)
                .endDate(endDate)
                .isRecruiting(isRecruiting)
                .maxMember(maxMember)
                .hashTags(hashTags)
                .member(member)
                .study(study)
                .clipCount(clipCount)
                .build();

        return recruitmentArticleRepository.save(recruitmentArticle);
    }

    public RecruitmentArticle updateRecruitmentArticleHashTags(Long articleId, String newHashTags) {
        RecruitmentArticle article = recruitmentArticleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("RecruitmentArticle not found"));

        // 기존 해시태그와 새 해시태그를 병합하여 업데이트
        String mergedHashTags = HashTagUtil.mergeHashTags(article.getHashTags(), newHashTags);
        article.setHashTags(mergedHashTags);

        return recruitmentArticleRepository.save(article);
    }

    public RecruitmentArticle findById(Long id) {
        return recruitmentArticleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RecruitmentArticle not found"));
    }

    @Transactional
    public void updateClipCounter(RecruitmentArticle recruitmentArticle, String upOrDown) {
        Integer clipCount = recruitmentArticle.getClipCount();
        if (upOrDown.equals("up")) {
            clipCount++;
        } else if (upOrDown.equals("down")) {
            clipCount--;
        }
        recruitmentArticle.setClipCount(clipCount);
    }
}
