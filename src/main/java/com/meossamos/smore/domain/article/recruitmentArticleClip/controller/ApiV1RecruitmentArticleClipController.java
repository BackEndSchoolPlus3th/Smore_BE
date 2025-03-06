package com.meossamos.smore.domain.article.recruitmentArticleClip.controller;

import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.article.recruitmentArticleClip.entity.RecruitmentArticleClip;
import com.meossamos.smore.domain.article.recruitmentArticleClip.dto.RecruitmentArticleClipRequestDTO;
import com.meossamos.smore.domain.article.recruitmentArticleClip.service.RecruitmentArticleClipService;
import com.meossamos.smore.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/recruitmentArticle")
@RequiredArgsConstructor
public class ApiV1RecruitmentArticleClipController {
    private final RecruitmentArticleClipService recruitmentArticleClipService;
    private final RecruitmentArticleService recruitmentArticleService;
    private final MemberService memberService;

    // 모집글 클립
    @PostMapping("/clip")
    public ResponseEntity<?> clipRecruitmentArticle(
            @RequestBody Map<String, Long> body
            ) {
        System.out.println(body);
        long devMemberId = 1L; // 테스트용 devMemberId
//        long recruitmentArticleId = Long.parseLong(body.get("recruitmentArticleId"));
        if (recruitmentArticleClipService.isClipped(body.get("recruitmentArticleId"), devMemberId)) {
            // 이미 클립한 모집글 409 error
            return ResponseEntity.badRequest().build();
        }
        RecruitmentArticleClip recruitmentArticleClip =
                recruitmentArticleClipService.save(body.get("recruitmentArticleId"),devMemberId);
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
            @RequestParam(value = "recruitmentArticleId") long recruitmentArticleId
    ) {
        long devMemberId = 1L; // 테스트용 devMemberId
        if (!recruitmentArticleClipService.isClipped(recruitmentArticleId, devMemberId)) {
            // 클립하지 않은 모집글 409 error
            return ResponseEntity.badRequest().build();
        }
        boolean result = recruitmentArticleClipService.delete(recruitmentArticleId, devMemberId);
        if (result) {
            // 클립 취소 성공
            return ResponseEntity.ok().build();
        } else {
            // 클립 취소 실패
            return ResponseEntity.badRequest().build();
        }
    }
}
