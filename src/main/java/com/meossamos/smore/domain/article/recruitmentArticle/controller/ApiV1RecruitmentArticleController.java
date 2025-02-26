package com.meossamos.smore.domain.article.recruitmentArticle.controller;

import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiV1RecruitmentArticleController {
    private final RecruitmentArticleService recruitmentArticleService;

    @GetMapping("/recruitmentArticle/test")
    public RsData<?> test() {
        RecruitmentArticle testRecruitmentArticle = RecruitmentArticle.builder()
                .title("테스트")
                .content("테스트")
                .region("테스트")
                .imageUrls("테스트")
                .startDate(null)
                .endDate(null)
                .isRecruiting(true)
                .maxMember(1)
                .build();
        return new RsData<>("200", "테스트 성공", testRecruitmentArticle);
    }
}
