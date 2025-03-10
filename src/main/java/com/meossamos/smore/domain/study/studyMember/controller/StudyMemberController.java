package com.meossamos.smore.domain.study.studyMember.controller;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.domain.study.studyMember.dto.MyStudyListResponse;
import com.meossamos.smore.domain.study.studyMember.dto.StudyWithPositionSimpleDto;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StudyMemberController {
    private final StudyMemberService studyMemberService;
    private final StudyService studyService;

    // 유저 탈퇴
    @DeleteMapping("/study/{study_Id}/delete")
    public ResponseEntity<String> leaveStudy(@AuthenticationPrincipal Member member, @PathVariable("study_Id") Long studyId) {
            studyMemberService.leaveStudy(member, studyId);
            return new ResponseEntity<>("스터디에서 탈퇴했습니다.", HttpStatus.OK);
    }

    @GetMapping("/current-user")
    public ResponseEntity<Long> getCurrentUserId() {
        Long userId = studyMemberService.getAuthenticatedMemberId();
        return ResponseEntity.ok(userId);
    }

    // 한 멤버의 모든 스터디 조회
    @GetMapping("/my-study")
    public ResponseEntity<?> getStudyList (
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        List<StudyWithPositionSimpleDto> studyWithPositionSimpleDtoList = studyMemberService.getStudiesWithPositionByMemberId(memberId);
        List<MyStudyListResponse> myStudyListResponseList = studyWithPositionSimpleDtoList.stream()
                .map(studyWithPositionSimpleDto -> {
                    return MyStudyListResponse.builder()
                            .id(studyWithPositionSimpleDto.getStudy().getId())
                            .title(studyWithPositionSimpleDto.getStudy().getTitle())
                            .introduction(studyWithPositionSimpleDto.getStudy().getIntroduction())
                            .thumbnailUrl(studyWithPositionSimpleDto.getStudy().getThumbnailUrl())
                            .studyPosition(studyWithPositionSimpleDto.getPosition())
                            .hashTags(studyWithPositionSimpleDto.getStudy().getHashTags())
                            .memberCnt(studyWithPositionSimpleDto.getStudy().getMemberCnt())
                            .registrationDate(studyWithPositionSimpleDto.getCreatedDate().toLocalDate())
                            .build();
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(myStudyListResponseList);
    }


}
