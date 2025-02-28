package com.meossamos.smore.domain.article.recruitmentArticleHashTag.repository;

import com.meossamos.smore.domain.article.recruitmentArticleHashTag.entity.RecruitmentArticleHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentArticleHashTagRepository extends JpaRepository<RecruitmentArticleHashTag, Long> {
}
