package com.meossamos.smore.domain.study.studyMember.controller;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
}
