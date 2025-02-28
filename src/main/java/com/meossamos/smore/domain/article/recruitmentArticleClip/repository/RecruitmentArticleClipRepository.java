package com.meossamos.smore.domain.article.recruitmentArticleClip.repository;

import com.meossamos.smore.domain.article.recruitmentArticleClip.entity.RecruitmentArticleClip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentArticleClipRepository extends JpaRepository<RecruitmentArticleClip, Long> {
    List<RecruitmentArticleClip> findByMemberId(Long memberId);
    List<RecruitmentArticleClip> findByRecruitmentArticleId(Long recruitmentArticleId);
    RecruitmentArticleClip findByRecruitmentArticleIdAndMemberId(Long recruitmentArticleId, Long memberId);
    void deleteByRecruitmentArticleIdAndMemberId(Long recruitmentArticleId, Long memberId);
}
