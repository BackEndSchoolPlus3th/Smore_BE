package com.meossamos.smore.domain.article.recruitmentArticle.repository;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentArticleRepository extends JpaRepository<RecruitmentArticle, Long> {
}
