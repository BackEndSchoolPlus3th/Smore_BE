package com.meossamos.smore.domain.article.recruitmentArticleComment.service;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticleComment.dto.RecruitmentArticleDetailCommentResponseData;
import com.meossamos.smore.domain.article.recruitmentArticleComment.entity.RecruitmentArticleComment;
import com.meossamos.smore.domain.article.recruitmentArticleComment.repository.RecruitmentArticleCommentRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitmentArticleCommentService {
    private final RecruitmentArticleCommentRepository recruitmentArticleCommentRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public void deleteById(Long recruitmentArticleId, Long commentId) {
        recruitmentArticleCommentRepository.deleteById(commentId);
    }

    public boolean save(String comment, Long recruitmentArticleId, Long memberId) {
        RecruitmentArticle recruitmentArticle = entityManager.getReference(RecruitmentArticle.class, recruitmentArticleId);
        Member member = entityManager.getReference(Member.class, memberId);

        RecruitmentArticleComment articleComment = RecruitmentArticleComment.builder()
                .comment(comment)
                .recruitmentArticle(recruitmentArticle)
                .member(member)
                .build();


        recruitmentArticleCommentRepository.save(articleComment);
        return true;
    }

    // DTO Projection을 이용하여 댓글과 작성자 정보를 한 번에 조회하는 메서드
    public List<RecruitmentArticleDetailCommentResponseData> getCommentDetailsByArticleId(Long articleId) {
        return recruitmentArticleCommentRepository.findCommentDetailsByArticleId(articleId);
    }
}
