package com.meossamos.smore.domain.article.recruitmentArticleComment.repository;

import com.meossamos.smore.domain.article.recruitmentArticleComment.dto.RecruitmentArticleDetailCommentResponseData;
import com.meossamos.smore.domain.article.recruitmentArticleComment.entity.RecruitmentArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentArticleCommentRepository  extends JpaRepository<RecruitmentArticleComment, Long> {
    @Query("SELECT new com.meossamos.smore.domain.article.recruitmentArticleComment.dto.RecruitmentArticleDetailCommentResponseData(" +
            "c.id, c.comment, m.id, m.nickname, c.createdDate) " +
            "FROM RecruitmentArticleComment c " +
            "JOIN c.member m " +
            "WHERE c.recruitmentArticle.id = :articleId")
    List<RecruitmentArticleDetailCommentResponseData> findCommentDetailsByArticleId(@Param("articleId") Long articleId);
}
