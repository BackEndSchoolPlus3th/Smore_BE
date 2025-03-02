package com.meossamos.smore.domain.article.recruitmentArticleClip.controller;

import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.article.recruitmentArticleClip.entity.RecruitmentArticleClip;
import com.meossamos.smore.domain.article.recruitmentArticleClip.dto.RecruitmentArticleClipRequestDTO;
import com.meossamos.smore.domain.article.recruitmentArticleClip.service.RecruitmentArticleClipService;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
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
    public RsData<?> clipRecruitmentArticle(
            @RequestBody Map<String, String> body
            ) {
        System.out.println(body);
        long devMemberId = 1L; // 테스트용 devMemberId
        long recruitmentArticleId = Long.parseLong(body.get("recruitmentArticleId"));
        System.out.println(recruitmentArticleId);
        if (recruitmentArticleClipService.isClipped(recruitmentArticleId, devMemberId)) {
            return new RsData<>("400", "이미 클립한 모집글입니다.", null);
        }
        RecruitmentArticleClip recruitmentArticleClip =
                recruitmentArticleClipService.save(recruitmentArticleId,devMemberId);
        return new RsData<>("200", "모집글 클립 성공", recruitmentArticleClip);
    }


    // 모집글 클립 취소
    @DeleteMapping("/clip")
    public RsData<?> unClipRecruitmentArticle(
            @RequestParam(value = "recruitmentArticleId", defaultValue = "1") long recruitmentArticleId
    ) {
        long devMemberId = 1L; // 테스트용 devMemberId
        if (!recruitmentArticleClipService.isClipped(recruitmentArticleId, devMemberId)) {
            return new RsData<>("400", "클립하지 않은 모집글입니다.", null);
        }
        boolean result = recruitmentArticleClipService.delete(recruitmentArticleId, devMemberId);
        if (result) {
            return new RsData<>("200", "모집글 클립 취소 성공", result);
        } else {
            return new RsData<>("400", "모집글 클립 취소 실패", null);
        }
    }
}
