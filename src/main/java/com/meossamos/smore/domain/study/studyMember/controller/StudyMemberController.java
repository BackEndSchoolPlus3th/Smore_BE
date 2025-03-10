package com.meossamos.smore.domain.study.studyMember.controller;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.domain.study.studyMember.dto.AddStudyMemberDto;
import com.meossamos.smore.domain.study.studyMember.dto.MyStudyListResponse;
import com.meossamos.smore.domain.study.studyMember.dto.StudyWithPositionSimpleDto;
import com.meossamos.smore.domain.study.studyMember.dto.UpdateStudyMemberPermissionDto;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
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

    /**
     * 멤버 추가 (리더 전용)
     * POST /api/v1/study/{studyId}/members
     */
    @PostMapping("/study/{studyId}/members")
    public ResponseEntity<?> addMember(@PathVariable Long studyId,
                                       @RequestBody AddStudyMemberDto addStudyMemberDto,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Long leaderMemberId = Long.parseLong(userDetails.getUsername());
        try {
            StudyMember newStudyMember = studyMemberService.addMember(studyId, leaderMemberId, addStudyMemberDto.getMemberId());
            return ResponseEntity.ok(newStudyMember);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 강퇴: 스터디 리더가 특정 멤버를 강퇴
     * DELETE /api/v1/study/{studyId}/members/{memberId}/kick
     */
    @DeleteMapping("/study/{studyId}/members/{memberId}/kick")
    public ResponseEntity<?> removeMember(@PathVariable Long studyId,
                                          @PathVariable Long memberId,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Long leaderMemberId = Long.parseLong(userDetails.getUsername());
        try {
            studyMemberService.removeMember(studyId, leaderMemberId, memberId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 탈퇴: 본인이 스터디에서 탈퇴
     * DELETE /api/v1/study/{studyId}/members/leave
     */
    @DeleteMapping("/study/{studyId}/members/leave")
    public ResponseEntity<?> leaveStudy(@PathVariable Long studyId,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Long memberId = Long.parseLong(userDetails.getUsername());
        try {
            studyMemberService.leaveStudy(studyId, memberId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 권한 변경: 스터디 리더가 특정 멤버의 권한을 변경
     * PUT /api/v1/study/{studyId}/members/{memberId}/permissions
     */
    @PatchMapping("/study/{studyId}/members/{memberId}/permissions")
    public ResponseEntity<?> updatePermissions(@PathVariable Long studyId,
                                               @PathVariable Long memberId,
                                               @RequestBody UpdateStudyMemberPermissionDto dto,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Long leaderMemberId = Long.parseLong(userDetails.getUsername());
        try {
            StudyMember updatedMember = studyMemberService.updateMemberPermissions(studyId, leaderMemberId, memberId, dto);
            return ResponseEntity.ok(updatedMember);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 권한 조회: 특정 멤버의 권한 정보를 조회
     * GET /api/v1/study/{studyId}/members/{memberId}/permissions
     */
    @GetMapping("/study/{studyId}/members/{memberId}/permissions")
    public ResponseEntity<?> getPermissions(@PathVariable Long studyId,
                                            @PathVariable Long memberId,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            StudyMember studyMember = studyMemberService.getMemberPermissions(studyId, memberId);
            return ResponseEntity.ok(studyMember);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
