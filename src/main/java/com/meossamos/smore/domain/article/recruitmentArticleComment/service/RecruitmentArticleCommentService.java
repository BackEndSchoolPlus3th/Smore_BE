package com.meossamos.smore.domain.article.recruitmentArticleComment.service;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticleComment.dto.RecruitmentArticleDetailCommentResponseData;
import com.meossamos.smore.domain.article.recruitmentArticleComment.dto.RecruitmentArticleDetailCommentWithProfileResponseData;
import com.meossamos.smore.domain.article.recruitmentArticleComment.entity.RecruitmentArticleComment;
import com.meossamos.smore.domain.article.recruitmentArticleComment.repository.RecruitmentArticleCommentRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitmentArticleCommentService {
    private final RecruitmentArticleCommentRepository recruitmentArticleCommentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // 댓글 생성: 불필요한 DB 조회를 막기 위해 getReference 사용
    @Transactional
    public RecruitmentArticleComment createComment(String comment, Long recruitmentArticleId, Long memberId) {
        // getReference를 사용하면 실제 DB 조회 없이 프록시 객체를 반환
        var recruitmentArticle = entityManager.getReference(
                RecruitmentArticle.class, recruitmentArticleId);
        var member = entityManager.getReference(
                Member.class, memberId);

        RecruitmentArticleComment articleComment = RecruitmentArticleComment.builder()
                .comment(comment)
                .recruitmentArticle(recruitmentArticle)
                .member(member)
                .build();
        return recruitmentArticleCommentRepository.save(articleComment);
    }

    // 댓글 세부 정보를 조회하는 메서드 (프로필 이미지 포함)
    public List<RecruitmentArticleDetailCommentWithProfileResponseData> getCommentDetailsWithProfileByArticleId(Long articleId) {
        return recruitmentArticleCommentRepository.findCommentDetailsWithProfileByArticleId(articleId);
    }

    // 댓글 업데이트: 한 번의 JPQL 업데이트 쿼리로 처리
    @Transactional
    public int updateComment(Long commentId, String comment, Long memberId) {
        return recruitmentArticleCommentRepository.updateCommentByIdAndMemberId(commentId, comment, memberId);
    }

    //  댓글 삭제: JPQL @Modifying 쿼리로 댓글 작성자 검증 및 삭제를 한 번에 수행
    @Transactional
    public int deleteComment(Long commentId, Long memberId) {
        return recruitmentArticleCommentRepository.deleteByIdAndMemberId(commentId, memberId);
    }
}
