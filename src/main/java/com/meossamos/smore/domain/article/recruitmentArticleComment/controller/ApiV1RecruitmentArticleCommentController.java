package com.meossamos.smore.domain.article.recruitmentArticleComment.controller;

import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.article.recruitmentArticleComment.dto.NewRecruitmentArticleCommentDto;
import com.meossamos.smore.domain.article.recruitmentArticleComment.dto.RecruitmentArticleDetailCommentResponseData;
import com.meossamos.smore.domain.article.recruitmentArticleComment.dto.RecruitmentArticleDetailCommentWithProfileResponseData;
import com.meossamos.smore.domain.article.recruitmentArticleComment.service.RecruitmentArticleCommentService;
import com.meossamos.smore.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
            @PathVariable Long recruitmentArticleId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long memberId = userDetails != null ? Long.parseLong(userDetails.getUsername()) : null;

        // 프로필 정보를 포함한 댓글 데이터 조회
        List<RecruitmentArticleDetailCommentWithProfileResponseData> commentList =
                recruitmentArticleCommentService.getCommentDetailsWithProfileByArticleId(recruitmentArticleId);

        // 각 댓글을 변환하며, 로그인한 사용자와 작성자 비교 후 editable 필드 설정
        List<RecruitmentArticleDetailCommentResponseData> commentListResponseData =
                commentList.stream()
                        .map(comment -> RecruitmentArticleDetailCommentResponseData.builder()
                                .id(comment.getId())
                                .comment(comment.getComment())
                                .writerName(comment.getWriterName())
                                .writerProfileImageUrl(comment.getWriterProfileImageUrl())
                                .createdDate(comment.getCreatedDate())
                                .editable(comment.getWriterId().equals(memberId))
                                .build())
                        .collect(Collectors.toList());

        return ResponseEntity.ok().body(commentListResponseData);
    }

    // 댓글 생성
    @PostMapping
    public ResponseEntity<?> createRecruitmentArticleComment(
            @PathVariable Long recruitmentArticleId,
            @RequestBody NewRecruitmentArticleCommentDto newRecruitmentArticleCommentDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 로그인 상태가 아니면 401 Unauthorized
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Long memberId = Long.parseLong(userDetails.getUsername());

        recruitmentArticleCommentService.createComment(
                newRecruitmentArticleCommentDto.getComment(), recruitmentArticleId, memberId
        );

        return ResponseEntity.ok().build();
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<?> updateRecruitmentArticleComment(
            @PathVariable Long recruitmentArticleId,
            @PathVariable Long commentId,
            @RequestBody NewRecruitmentArticleCommentDto newRecruitmentArticleCommentDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 로그인 상태 체크
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Long memberId = Long.parseLong(userDetails.getUsername());

        // JPQL 업데이트 쿼리 실행, 조건(댓글 작성자 일치)을 만족하지 않으면 0 반환됨
        int updatedCount = recruitmentArticleCommentService.updateComment(
                commentId,
                newRecruitmentArticleCommentDto.getComment(),
                memberId
        );

        // 업데이트된 행이 없으면 403 에러(권한 미달 혹은 댓글이 존재하지 않음)
        if (updatedCount == 0) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok().build();
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteRecruitmentArticleComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 로그인 상태가 아니면 401 Unauthorized
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Long memberId = Long.parseLong(userDetails.getUsername());

        int deletedCount = recruitmentArticleCommentService.deleteComment(commentId, memberId);

        // 삭제된 행이 없으면(조건에 맞는 댓글이 없거나 작성자가 아니면) 403 Forbidden 반환
        if (deletedCount == 0) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok().build();
    }
}
