package com.meossamos.smore.domain.article.recruitmentArticleComment.controller;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.article.recruitmentArticleComment.dto.NewRecruitmentArticleCommentDto;
import com.meossamos.smore.domain.article.recruitmentArticleComment.repository.RecruitmentArticleCommentRepository;
import com.meossamos.smore.domain.article.recruitmentArticleComment.service.RecruitmentArticleCommentService;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruitmentArticles/{recruitmentArticleId}/comments")
@RequiredArgsConstructor
public class ApiV1RecruitmentArticleCommentController {
    private final RecruitmentArticleCommentService recruitmentArticleCommentService;
    private final RecruitmentArticleService recruitmentArticleService;
    private final MemberService memberService;

    @GetMapping
    public RsData<?> getRecruitmentArticleComments(
            @RequestParam(value = "recruitmentArticleId") Long recruitmentArticleId
    ) {
        System.out.println("RecruitmentArticleId: " + recruitmentArticleId);
        return new RsData<>("200", "댓글 목록 조회 성공", recruitmentArticleCommentService.getCommentDetailsByArticleId(recruitmentArticleId));
    }

    @PostMapping
    public RsData<?> createRecruitmentArticleComment(
            @PathVariable Long recruitmentArticleId,
            @RequestBody NewRecruitmentArticleCommentDto newRecruitmentArticleCommentDto
    ) {
        Long memberId = 1L;
        System.out.println("RecruitmentArticleId: " + recruitmentArticleId);
        System.out.println("Comment: " + newRecruitmentArticleCommentDto.getComment());
        recruitmentArticleCommentService.save(newRecruitmentArticleCommentDto.getComment(), recruitmentArticleId, memberId);

        return new RsData<>("200", "댓글 작성 성공", null);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<RsData<?>> deleteRecruitmentArticleComment(
            @PathVariable Long recruitmentArticleId,
            @PathVariable Long commentId
    ) {
        recruitmentArticleCommentService.deleteById(recruitmentArticleId, commentId);

        return ResponseEntity.ok(new RsData<>("200", "댓글 삭제 성공", null));
    }
}
