package com.meossamos.smore.domain.article.studyArticle.repository;

import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyArticleRepository extends JpaRepository<StudyArticle, Long> {
}
