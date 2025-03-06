package com.meossamos.smore.domain.article.recruitmentArticleComment.controller;

import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.article.recruitmentArticleComment.dto.NewRecruitmentArticleCommentDto;
import com.meossamos.smore.domain.article.recruitmentArticleComment.dto.RecruitmentArticleDetailCommentResponseData;
import com.meossamos.smore.domain.article.recruitmentArticleComment.service.RecruitmentArticleCommentService;
import com.meossamos.smore.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recruitmentArticles/{recruitmentArticleId}/comments")
@RequiredArgsConstructor
public class ApiV1RecruitmentArticleCommentController {
    private final RecruitmentArticleCommentService recruitmentArticleCommentService;
    private final RecruitmentArticleService recruitmentArticleService;
    private final MemberService memberService;

    // 댓글 조회
    @GetMapping
    public ResponseEntity<?> getRecruitmentArticleComments(
            @RequestParam(value = "recruitmentArticleId") Long recruitmentArticleId
    ) {
        System.out.println("RecruitmentArticleId: " + recruitmentArticleId);
        List<RecruitmentArticleDetailCommentResponseData> recruitmentArticleDetailCommentResponseDataList = recruitmentArticleCommentService.getCommentDetailsByArticleId(recruitmentArticleId);
        return ResponseEntity.ok().body(recruitmentArticleDetailCommentResponseDataList);
    }

    // 댓글 생성
    @PostMapping
    public ResponseEntity<?> createRecruitmentArticleComment(
            @PathVariable Long recruitmentArticleId,
            @RequestBody NewRecruitmentArticleCommentDto newRecruitmentArticleCommentDto
    ) {
        Long memberId = 1L;
        System.out.println("RecruitmentArticleId: " + recruitmentArticleId);
        System.out.println("Comment: " + newRecruitmentArticleCommentDto.getComment());
        recruitmentArticleCommentService.save(newRecruitmentArticleCommentDto.getComment(), recruitmentArticleId, memberId);

        return ResponseEntity.ok().build();
    }

    // 댓글 수정
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteRecruitmentArticleComment(
            @PathVariable Long recruitmentArticleId,
            @PathVariable Long commentId
    ) {
        recruitmentArticleCommentService.deleteById(recruitmentArticleId, commentId);

        return ResponseEntity.ok().build();
    }
}
