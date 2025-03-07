package com.meossamos.smore.domain.study.studyMember.controller;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.domain.study.studyMember.dto.MyStudyListResponse;
import com.meossamos.smore.domain.study.studyMember.dto.StudyWithPositionDto;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StudyMemberController {
    private final StudyMemberService studyMemberService;
    private final StudyService studyService;

    // 유저 탈퇴
    @DeleteMapping("/api/study/{study_Id}/delete")
    public ResponseEntity<String> leaveStudy(@AuthenticationPrincipal Member member, @PathVariable("study_Id") Long studyId) {
            studyMemberService.leaveStudy(member, studyId);
            return new ResponseEntity<>("스터디에서 탈퇴했습니다.", HttpStatus.OK);
    }

    @GetMapping("/api/current-user")
    public ResponseEntity<Long> getCurrentUserId() {
        Long userId = studyMemberService.getAuthenticatedMemberId();
        return ResponseEntity.ok(userId);
    }

    // 한 멤버의 모든 스터디 조회
    @GetMapping("/v1/my-study")
    public ResponseEntity<?> getStudyList () {
        Long devMemberId = 1001L;
        List<StudyWithPositionDto> studyWithPositionDtoList = studyMemberService.getStudiesWithPositionByMemberId(devMemberId);
        List<MyStudyListResponse> myStudyListResponseList = studyWithPositionDtoList.stream()
                .map(studyWithPositionDto -> {
                    return MyStudyListResponse.builder()
                            .id(studyWithPositionDto.getStudy().getId())
                            .title(studyWithPositionDto.getStudy().getTitle())
                            .introduction(studyWithPositionDto.getStudy().getIntroduction())
                            .thumbnailUrl(studyWithPositionDto.getStudy().getImageUrls().split(",")[0])
                            .studyPosition(studyWithPositionDto.getPosition())
                            .hashTags(studyWithPositionDto.getStudy().getHashTags())
                            .memberCnt(studyWithPositionDto.getStudy().getMemberCnt())
                            .registrationDate(studyWithPositionDto.getCreatedDate().toLocalDate())
                            .build();
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(myStudyListResponseList);
    }
}
