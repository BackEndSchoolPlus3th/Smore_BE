package com.meossamos.smore.domain.article.recruitmentArticleClip.service;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.article.recruitmentArticleClip.dto.RecruitmentArticleClipResponseDTO;
import com.meossamos.smore.domain.article.recruitmentArticleClip.entity.RecruitmentArticleClip;
import com.meossamos.smore.domain.article.recruitmentArticleClip.repository.RecruitmentArticleClipRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitmentArticleClipService {
    private final RecruitmentArticleClipRepository recruitmentArticleClipRepository;
    private final MemberService memberService;
    private final RecruitmentArticleService recruitmentArticleService;

    // 모집글 클립 저장
    public RecruitmentArticleClip save(Long recruitmentArticleId, Long memberId) {
        Member member = memberService.findById(memberId);
        RecruitmentArticle recruitmentArticle = recruitmentArticleService.findById(recruitmentArticleId);
        RecruitmentArticleClip recruitmentArticleClip = RecruitmentArticleClip.builder()
                .recruitmentArticle(recruitmentArticle)
                .member(member)
                .build();

        RecruitmentArticleClip recruitmentArticleClipResult = recruitmentArticleClipRepository.save(recruitmentArticleClip);
        recruitmentArticleService.updateClipCounter(recruitmentArticle, "up");
        return recruitmentArticleClipResult;
    }

    // 모집글 클립 삭제
    @Transactional
    public boolean delete(Long recruitmentArticleId, Long memberId) {
        if (isClipped(recruitmentArticleId, memberId)) {
            // 삭제 성공
            recruitmentArticleClipRepository.deleteByRecruitmentArticleIdAndMemberId(recruitmentArticleId, memberId);
            recruitmentArticleService.updateClipCounter(recruitmentArticleService.findById(recruitmentArticleId), "down");
            return true;
        } else {
            // 삭제 실패
            return false;
        }
    }

    // 모집글 클립 여부 확인
    public boolean isClipped(Long recruitmentArticleId, Long memberId) {
        return recruitmentArticleClipRepository.findByRecruitmentArticleIdAndMemberId(recruitmentArticleId, memberId) != null;
    }

    // 사용자 클립한 모집글 정보와 클립 생성일자 조회
    public List<RecruitmentArticleClipResponseDTO> getClippedRecruitmentArticlesWithClipInfo(Long memberId) {
        return recruitmentArticleClipRepository.findClippedRecruitmentArticleDetailsByMemberId(memberId);
    }
}
