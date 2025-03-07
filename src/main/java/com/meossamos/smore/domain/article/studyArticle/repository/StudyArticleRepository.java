package com.meossamos.smore.domain.article.studyArticle.repository;

import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import com.meossamos.smore.domain.study.study.entity.Study;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyArticleRepository extends JpaRepository<StudyArticle, Long> {
    List<StudyArticle> findByStudyId(Long studyId, Sort sort);

    @Query("SELECT sa FROM StudyArticle sa WHERE sa.study = :study" +
            " AND (:title IS NULL OR sa.title LIKE %:title%)" +
            " AND (:content IS NULL OR sa.content LIKE %:content%)")
    List<StudyArticle> findByStudyAndSearchConditions(Study study, String title, String content);
}
