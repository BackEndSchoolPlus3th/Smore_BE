package com.meossamos.smore.domain.article.recruitmentArticle.repository;

import com.meossamos.smore.domain.article.recruitmentArticle.dto.SimpleRecruitmentDto;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitmentArticleRepository extends JpaRepository<RecruitmentArticle, Long> {
    @Query("select ra from RecruitmentArticle ra join fetch ra.member where ra.id = :id")
    Optional<RecruitmentArticle> findByIdWithMember(@Param("id") Long id);

    @Query("select new com.meossamos.smore.domain.article.recruitmentArticle.dto.SimpleRecruitmentDto(" +
            "ra.id, ra.title, ra.introduction, ra.thumbnailUrl, ra.isRecruiting, " +
            "m.nickname, m.profileImageUrl, ra.clipCount, ra.hashTags) " +
            "from RecruitmentArticle ra join ra.member m " +
            "where ra.study.id = :studyId")
    List<SimpleRecruitmentDto> findSimpleRecruitmentsByStudyId(@Param("studyId") Long studyId);

    @Query("select ra from RecruitmentArticle ra join fetch ra.member join fetch ra.study s join fetch s.leader where ra.id = :id")
    Optional<RecruitmentArticle> findByIdWithMemberAndStudyLeader(@Param("id") Long id);
}
