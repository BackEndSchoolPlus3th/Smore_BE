package com.meossamos.smore.domain.article.recruitmentArticle.repository;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruitmentArticleRepository extends JpaRepository<RecruitmentArticle, Long> {
    @Query("select ra from RecruitmentArticle ra join fetch ra.member where ra.id = :id")
    Optional<RecruitmentArticle> findByIdWithMember(@Param("id") Long id);
}
