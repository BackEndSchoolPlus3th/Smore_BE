package com.meossamos.smore.domain.study.studyMember.controller;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.domain.study.studyMember.dto.AddMemberRequest;
import com.meossamos.smore.domain.study.studyMember.dto.MyStudyListResponse;
import com.meossamos.smore.domain.study.studyMember.dto.StudyMemberDto;
import com.meossamos.smore.domain.study.studyMember.dto.StudyWithPositionSimpleDto;
import com.meossamos.smore.domain.study.studyMember.repository.StudyMemberRepository;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class StudyMemberController {
    private final StudyMemberService studyMemberService;
    private final StudyService studyService;
    private final StudyMemberRepository studyMemberRepository;

    // 유저 탈퇴
    @DeleteMapping("/study/{study_Id}/delete")
    public ResponseEntity<String> leaveStudy(@AuthenticationPrincipal Member member, @PathVariable("study_Id") Long studyId) {
            studyMemberService.leaveStudy(member, studyId);
            return new ResponseEntity<>("스터디에서 탈퇴했습니다.", HttpStatus.OK);
    }

    // 로그인 유저 id 조회
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

    // 유저를 스터디에 추가하고 권한을 설정하는 API
    @PostMapping("/study/{studyId}/addMember")
    public ResponseEntity<String> addMemberToStudy(@PathVariable Long studyId, @RequestBody AddMemberRequest request) {
        studyMemberService.addMemberToStudy(
                studyId,
                request.getMemberId(),
                request.getRole(),
                request.getRecruitManage(),
                request.getArticleManage(),
                request.getCalendarManage(),
                request.getSettingManage()
        );
        return ResponseEntity.ok("User added to study with role: " + request.getRole());
    }

    // 유저 권한 조회 API
    @GetMapping("/study/{studyId}/checkPermission")
    public ResponseEntity<Map<String, Boolean>> checkPermission(
            @PathVariable Long studyId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long memberId = Long.valueOf(userDetails.getUsername());

        Map<String, Boolean> permissions = new HashMap<>();
        permissions.put("recruitManage", studyMemberService.hasPermission(studyId, memberId, "recruitManage"));
        permissions.put("articleManage", studyMemberService.hasPermission(studyId, memberId, "articleManage"));
        permissions.put("calendarManage", studyMemberService.hasPermission(studyId, memberId, "calendarManage"));
        permissions.put("settingManage", studyMemberService.hasPermission(studyId, memberId, "settingManage"));

        return ResponseEntity.ok(permissions);
    }

    // 스터디 모든 멤버 권한 확인
    @GetMapping("/study/{studyId}/members")
    public List<StudyMemberDto> getStudyMembers(@PathVariable Long studyId) {
        return studyMemberService.getStudyMembers(studyId);
    }

    // 권한 추가
    @PutMapping("/study/{studyId}/permissions")
    public ResponseEntity<String> updatePermissions(
            @PathVariable Long studyId,
            @RequestBody Map<Long, Map<String, Boolean>> updatedPermissions) {

        try {
            // 각 멤버에 대해 권한을 업데이트 처리
            studyMemberService.updatePermissions(studyId, updatedPermissions);

            return ResponseEntity.ok("Permissions updated successfully.");
        } catch (Exception e) {
            // 예외 발생 시 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update permissions: " + e.getMessage());
        }
    }

    // 권한 삭제
    @DeleteMapping("/study/{studyId}/permissions")
    public ResponseEntity<String> deletePermissions(
            @PathVariable Long studyId,
            @RequestBody Map<String, List<Long>> permissionsToDelete) {

        try {
            // 각 멤버에 대해 권한을 삭제 처리
            studyMemberService.deletePermissions(studyId, permissionsToDelete);

            return ResponseEntity.ok("Permissions deleted successfully.");
        } catch (Exception e) {
            // 예외 발생 시 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete permissions: " + e.getMessage());
        }
    }
}
