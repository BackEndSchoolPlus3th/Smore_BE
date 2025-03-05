package com.meossamos.smore.domain.article.recruitmentArticle.controller;

import com.meossamos.smore.domain.article.recruitmentArticle.dto.NewRecruitmentArticleDto;
import com.meossamos.smore.domain.article.recruitmentArticle.dto.RecruitmentArticleDetailResponseData;
import com.meossamos.smore.domain.article.recruitmentArticle.dto.RecruitmentArticleResponseData;
import com.meossamos.smore.domain.article.recruitmentArticle.dto.RecruitmentArticleSearchDto;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticleDoc;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleDocService;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.article.recruitmentArticleClip.service.RecruitmentArticleClipService;
import com.meossamos.smore.domain.article.recruitmentArticleComment.service.RecruitmentArticleCommentService;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiV1RecruitmentArticleController {
    private final RecruitmentArticleService recruitmentArticleService;
    private final RecruitmentArticleDocService recruitmentArticleDocService;
    private final MemberService memberService;
    private final RecruitmentArticleClipService recruitmentArticleClipService;
    private final RecruitmentArticleCommentService recruitmentArticleCommentService;

    @GetMapping("/recruitmentArticles")
    public RsData<?> getRecruitmentArticles(
            RecruitmentArticleSearchDto searchDto
    ) {
        List<String> titleList = searchDto.getTitleList();
        List<String> contentList = searchDto.getContentList();
        List<String> introductionList = searchDto.getIntroductionList();
        List<String> regionList = searchDto.getRegionList();
        List<String> hashTagList = searchDto.getHashTagsList();

        System.out.println("Titles: " + titleList);
        System.out.println("Contents: " + contentList);
        System.out.println("Introductions: " + introductionList);
        System.out.println("Regions: " + regionList);
        System.out.println("HashTags: " + hashTagList);

        List<RecruitmentArticleDoc> resultPage = recruitmentArticleDocService.findByTitleOrContentOrIntroductionOrRegionOrHashTags(titleList, contentList, introductionList, regionList, hashTagList, searchDto.getPage(), searchDto.getSize());
        List<RecruitmentArticleResponseData> recruitmentArticleResponseDataList = recruitmentArticleDocService.convertToResponseData(resultPage);

        return new RsData<>("200", "모집글 목록 조회 성공", recruitmentArticleResponseDataList);
    }

    @GetMapping("/recruitmentArticles/detail")
    public RsData<?> getRecruitmentArticleDetail(
            @RequestParam(value = "recruitmentArticleId") Long recruitmentArticleId
    ) {
        long devMemberId = 1L;
        RecruitmentArticle recruitmentArticle = recruitmentArticleService.findById(recruitmentArticleId);
        Member writer = memberService.findById(recruitmentArticle.getMember().getId());
        Member user = memberService.findById(devMemberId);

        boolean isClipped = recruitmentArticleClipService.isClipped(recruitmentArticleId, devMemberId);

        RecruitmentArticleDetailResponseData recruitmentArticleResponseData = RecruitmentArticleDetailResponseData.builder()
                .id(recruitmentArticle.getId())
                .title(recruitmentArticle.getTitle())
                .content(recruitmentArticle.getContent())
                .introduction(recruitmentArticle.getIntroduction())
                .region(recruitmentArticle.getRegion())
                .imageUrls(recruitmentArticle.getImageUrls())
                .startDate(recruitmentArticle.getStartDate().toString())
                .endDate(recruitmentArticle.getEndDate().toString())
                .isRecruiting(recruitmentArticle.getIsRecruiting())
                .createdDate(recruitmentArticle.getCreatedDate())
                .maxMember(recruitmentArticle.getMaxMember())
                .hashTags(recruitmentArticle.getHashTags())
                .clipCount(recruitmentArticle.getClipCount())
                .isClipped(isClipped)
                .writerName(writer.getNickname())
                .writerProfileImageUrl(writer.getProfileImageUrl())
                .build();

        return new RsData<>("200", "모집글 상세 조회 성공", recruitmentArticleResponseData);
    }


    @PostMapping("/study/{studyId}/recruitmentArticle")
    public RsData<?> createRecruitmentArticle(
            @PathVariable("studyId") Long studyId,
            @ModelAttribute NewRecruitmentArticleDto dto
    ) {
        Long devMemberId = 1L;
        // 예시: 전달받은 데이터 출력 (실제 서비스 로직에서는 dto를 바탕으로 저장 처리)
        System.out.println("Title: " + dto.getTitle());
        System.out.println("Content: " + dto.getContent());
        System.out.println("Introduction: " + dto.getIntroduction());
        System.out.println("Region: " + dto.getRegion());
        System.out.println("Start Date: " + dto.getStartDate());
        System.out.println("End Date: " + dto.getEndDate());
        System.out.println("Hashtags: " + dto.getHashtagList());
        System.out.println("Thumbnail file name: " + dto.getThumbnailUrl());


        RecruitmentArticle recruitmentArticle = recruitmentArticleService.save(dto.getTitle(), dto.getContent(), dto.getIntroduction(), dto.getRegion(), dto.getThumbnailUrl(), dto.getImageUrls(), dto.getStartDate(), dto.getEndDate(), true, dto.getMaxMember(), dto.getHashtagList().toString(), devMemberId, studyId, 0);

        // 이후 dto에 담긴 데이터를 기반으로 서비스 호출 및 저장 처리
        // 예: recruitmentArticleService.createArticle(studyId, dto);

        return new RsData<>("200", "모집글 생성 성공", null);
    }
}
