package com.meossamos.smore.domain.article.recruitmentArticleClip.service;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.article.recruitmentArticleClip.entity.RecruitmentArticleClip;
import com.meossamos.smore.domain.article.recruitmentArticleClip.repository.RecruitmentArticleClipRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentArticleClipService {
    private final RecruitmentArticleClipRepository recruitmentArticleClipRepository;
    private final MemberService memberService;
    private final RecruitmentArticleService recruitmentArticleService;

    public RecruitmentArticleClip save(Long recruitmentArticleId, Long memberId) {
        Member member = memberService.findById(memberId);
        RecruitmentArticle recruitmentArticle = recruitmentArticleService.findById(recruitmentArticleId);
        RecruitmentArticleClip recruitmentArticleClip = RecruitmentArticleClip.builder()
                .recruitmentArticle(recruitmentArticle)
                .member(member)
                .build();

        RecruitmentArticleClip recruitmentArticleClipResult = recruitmentArticleClipRepository.save(recruitmentArticleClip);
        recruitmentArticleService.UpdateClipCounter(recruitmentArticle, "up");
        return recruitmentArticleClipResult;
    }

    public boolean delete(Long recruitmentArticleId, Long memberId) {
        if (recruitmentArticleClipRepository.findByRecruitmentArticleIdAndMemberId(recruitmentArticleId, memberId) != null) {
            // 삭제 성공
            recruitmentArticleClipRepository.deleteByRecruitmentArticleIdAndMemberId(recruitmentArticleId, memberId);
            recruitmentArticleService.UpdateClipCounter(recruitmentArticleService.findById(recruitmentArticleId), "down");
            return true;
        } else {
            // 삭제 실패
            return false;
        }
    }
}
