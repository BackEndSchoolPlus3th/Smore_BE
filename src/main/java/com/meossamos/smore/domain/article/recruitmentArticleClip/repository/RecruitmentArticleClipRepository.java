package com.meossamos.smore.domain.article.recruitmentArticleClip.repository;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticleClip.dto.RecruitmentArticleClipResponseDTO;
import com.meossamos.smore.domain.article.recruitmentArticleClip.entity.RecruitmentArticleClip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentArticleClipRepository extends JpaRepository<RecruitmentArticleClip, Long> {
    RecruitmentArticleClip findByRecruitmentArticleIdAndMemberId(Long recruitmentArticleId, Long memberId);
    void deleteByRecruitmentArticleIdAndMemberId(Long recruitmentArticleId, Long memberId);

    @Query("SELECT new com.meossamos.smore.domain.article.recruitmentArticleClip.dto.RecruitmentArticleClipResponseDTO(" +
            "r.recruitmentArticle.id, " +
            "r.recruitmentArticle.title, " +
            "r.recruitmentArticle.introduction, " +
            "r.recruitmentArticle.isRecruiting, " +
            "r.recruitmentArticle.hashTags) " +
            "FROM RecruitmentArticleClip r " +
            "WHERE r.member.id = :memberId")
    List<RecruitmentArticleClipResponseDTO> findClippedRecruitmentArticleDetailsByMemberId(@Param("memberId") Long memberId);

}
