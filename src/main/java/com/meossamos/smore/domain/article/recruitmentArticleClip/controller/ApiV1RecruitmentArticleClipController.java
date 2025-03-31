package com.meossamos.smore.domain.article.recruitmentArticleClip.controller;

import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.article.recruitmentArticleClip.dto.RecruitmentArticleClipResponseDTO;
import com.meossamos.smore.domain.article.recruitmentArticleClip.entity.RecruitmentArticleClip;
import com.meossamos.smore.domain.article.recruitmentArticleClip.service.RecruitmentArticleClipService;
import com.meossamos.smore.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/recruitmentArticle")
@RequiredArgsConstructor
public class ApiV1RecruitmentArticleClipController {
    private final RecruitmentArticleClipService recruitmentArticleClipService;

    // 모집글 클립
    @PostMapping("/clip")
    public ResponseEntity<?> clipRecruitmentArticle(
            @RequestBody Map<String, Long> body,
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().build();
        }
        Long memberId = Long.parseLong(userDetails.getUsername());
        if (recruitmentArticleClipService.isClipped(body.get("recruitmentArticleId"), memberId)) {
            // 이미 클립한 모집글 409 error
            return ResponseEntity.badRequest().build();
        }
        RecruitmentArticleClip recruitmentArticleClip =
                recruitmentArticleClipService.save(body.get("recruitmentArticleId"),memberId);
        if (recruitmentArticleClip == null) {
            // 클립 실패
            return ResponseEntity.badRequest().build();
        } else {
            // 클립 성공
            return ResponseEntity.ok().build();
        }
    }


    // 모집글 클립 취소
    @DeleteMapping("/clip")
    public ResponseEntity<?> unClipRecruitmentArticle(
            @RequestParam(value = "recruitmentArticleId") long recruitmentArticleId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().build();
        }
        Long memberId = Long.parseLong(userDetails.getUsername());
        if (!recruitmentArticleClipService.isClipped(recruitmentArticleId, memberId)) {
            // 클립하지 않은 모집글 409 error
            return ResponseEntity.badRequest().build();
        }
        boolean result = recruitmentArticleClipService.delete(recruitmentArticleId, memberId);
        if (result) {
            // 클립 취소 성공
            return ResponseEntity.ok().build();
        } else {
            // 클립 취소 실패
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/clips")
    public ResponseEntity<List<RecruitmentArticleClipResponseDTO>> getClippedRecruitmentArticles(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.badRequest().build();
        }

        Long memberId = Long.parseLong(userDetails.getUsername());
        List<RecruitmentArticleClipResponseDTO> response = recruitmentArticleClipService.getClippedRecruitmentArticlesWithClipInfo(memberId);
        return ResponseEntity.ok(response);
    }
}
