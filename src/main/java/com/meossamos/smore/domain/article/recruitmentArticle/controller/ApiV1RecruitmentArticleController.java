package com.meossamos.smore.domain.article.recruitmentArticle.controller;

import com.meossamos.smore.domain.article.recruitmentArticle.dto.*;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticle;
import com.meossamos.smore.domain.article.recruitmentArticle.entity.RecruitmentArticleDoc;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleDocService;
import com.meossamos.smore.domain.article.recruitmentArticle.service.RecruitmentArticleService;
import com.meossamos.smore.domain.article.recruitmentArticleClip.service.RecruitmentArticleClipService;
import com.meossamos.smore.domain.article.recruitmentArticleComment.service.RecruitmentArticleCommentService;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;

import com.meossamos.smore.global.jwt.TokenProvider;
import com.meossamos.smore.global.util.ElasticSearchUtil;

import com.meossamos.smore.global.sse.SseEmitters;
import com.meossamos.smore.global.util.ElasticSearchUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiV1RecruitmentArticleController {
    private final RecruitmentArticleService recruitmentArticleService;
    private final RecruitmentArticleDocService recruitmentArticleDocService;
    private final MemberService memberService;
    private final RecruitmentArticleClipService recruitmentArticleClipService;
    private final RecruitmentArticleCommentService recruitmentArticleCommentService;
    private final SseEmitters sseEmitters;
    private final TokenProvider tokenProvider;
    @GetMapping("/recruitmentArticles")
    public ResponseEntity<?> getRecruitmentArticles(
            RecruitmentArticleSearchDto searchDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails != null) {
            System.out.println("user id: " + userDetails.getUsername());
        }

        List<String> titleList = searchDto.getTitleList();
        List<String> contentList = searchDto.getContentList();
        List<String> introductionList = searchDto.getIntroductionList();
        List<String> regionList = searchDto.getRegionList();
        List<String> hashTagList = searchDto.getHashTagsList();

        ElasticSearchUtil.SearchResult<RecruitmentArticleDoc> searchResult =
                recruitmentArticleDocService.findByTitleOrContentOrIntroductionOrRegionOrHashTags(
                        titleList, contentList, introductionList, regionList, hashTagList,
                        searchDto.getPage(), searchDto.getSize());

        List<RecruitmentArticleResponseData> responseData  = recruitmentArticleDocService.convertToResponseData(searchResult.getDocs());

        PagedResponse<RecruitmentArticleResponseData> pagedResponse =
                new PagedResponse<>(responseData, searchResult.getTotalHits(), searchDto.getPage(), searchDto.getSize());


        return ResponseEntity.ok(pagedResponse);
    }

    @GetMapping("/recruitmentArticles/detail")
    public ResponseEntity<?> getRecruitmentArticleDetail(
            @RequestParam(value = "recruitmentArticleId") Long recruitmentArticleId
    ) {
        long devMemberId = 1L;
        RecruitmentArticle recruitmentArticle = recruitmentArticleService.findById(recruitmentArticleId);
        Member writer = memberService.findById(recruitmentArticle.getMember().getId());
        Member user = memberService.findById(devMemberId);

        boolean isClipped = recruitmentArticleClipService.isClipped(recruitmentArticleId, devMemberId);

        RecruitmentArticleDetailResponseData responseData  = RecruitmentArticleDetailResponseData.builder()
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

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/recruitmentArticles/{recruitmentId}/apply")
    public ResponseEntity<?> createRecruitmentArticle( // 임시 생성. 후에 수정 필요
                                            @PathVariable("recruitmentId") Long recruitmentId,
                                            HttpServletRequest request) {
        String token = request.getHeader("authorization");
        //결론적으로 해당 게시물 작성자를 찾아야 그 유저에게 알림을 보낼 수 있다.

        Map<String, String> map = new HashMap<>();
        RecruitmentArticle recruitmentArticle = recruitmentArticleService.findById(recruitmentId);

        Long receiver = recruitmentArticle.getMember().getId();
        Long studyId = recruitmentArticle.getStudy().getId();
        Long senderId = Long.valueOf(tokenProvider.getAuthentication(token.substring(7)).getName());
        map.put("sender",memberService.findById(senderId).getNickname());
        map.put("senderToken",token.substring(7));
        map.put("receiver",receiver+"");
        map.put("recruitmentId",recruitmentId+"");
        map.put("studyId",studyId+"");

        //지원할 때랑 지원을 받는거는 지원받는 당사자만 알림을 받으면 되니까
        //emitter 중에서 해당 user만 찾아서 send해주면 된다.

        //서비스 부분에서 map을 리턴을 하고 바로 notiApplication 호출해주는 식으로 진행

        sseEmitters.notiApplication("application__reached",map,recruitmentId);
     return ResponseEntity.ok("지원 완료 "+recruitmentId);
    }

    @PostMapping("/study/{studyId}/recruitmentArticle")
    public ResponseEntity<?> createRecruitmentArticle(
            @PathVariable("studyId") Long studyId,
            @RequestBody NewRecruitmentArticleDto dto
    ) {
        Long devMemberId = 1L;

        String imageUrls = String.join(",", dto.getImageUrls());

        RecruitmentArticle recruitmentArticle = recruitmentArticleService.save(dto.getTitle(), dto.getContent(), dto.getIntroduction(), dto.getRegion(), dto.getThumbnailUrl(), imageUrls, dto.getStartDate(), dto.getEndDate(), true, dto.getMaxMember(), dto.getHashtags(), devMemberId, studyId, 0);

        // 이후 dto에 담긴 데이터를 기반으로 서비스 호출 및 저장 처리
        // 예: recruitmentArticleService.createArticle(studyId, dto);
        return  ResponseEntity.ok(recruitmentArticle.getId());
    }
}
