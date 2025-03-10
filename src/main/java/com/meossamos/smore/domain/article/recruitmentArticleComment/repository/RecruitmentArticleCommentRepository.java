package com.meossamos.smore.domain.article.recruitmentArticleComment.repository;

import com.meossamos.smore.domain.article.recruitmentArticleComment.dto.RecruitmentArticleDetailCommentResponseData;
import com.meossamos.smore.domain.article.recruitmentArticleComment.dto.RecruitmentArticleDetailCommentWithProfileResponseData;
import com.meossamos.smore.domain.article.recruitmentArticleComment.entity.RecruitmentArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentArticleCommentRepository  extends JpaRepository<RecruitmentArticleComment, Long> {
    // 댓글 세부 정보를 조회하는 메서드
    @Query("SELECT new com.meossamos.smore.domain.article.recruitmentArticleComment.dto.RecruitmentArticleDetailCommentWithProfileResponseData(" +
            "c.id, c.comment, m.id, m.nickname, m.profileImageUrl, c.createdDate) " +
            "FROM RecruitmentArticleComment c " +
            "JOIN c.member m " +
            "WHERE c.recruitmentArticle.id = :articleId")
    List<RecruitmentArticleDetailCommentWithProfileResponseData> findCommentDetailsWithProfileByArticleId(@Param("articleId") Long articleId);

    // 댓글 업데이트 메서드
    @Modifying
    @Query("UPDATE RecruitmentArticleComment c SET c.comment = :comment " +
            "WHERE c.id = :commentId AND c.member.id = :memberId")
    int updateCommentByIdAndMemberId(@Param("commentId") Long commentId,
                                     @Param("comment") String comment,
                                     @Param("memberId") Long memberId);

    // 댓글 삭제: 댓글 ID와 회원 ID가 일치하는 경우만 삭제
    @Modifying
    @Query("DELETE FROM RecruitmentArticleComment c WHERE c.id = :commentId AND c.member.id = :memberId")
    int deleteByIdAndMemberId(@Param("commentId") Long commentId, @Param("memberId") Long memberId);
}
