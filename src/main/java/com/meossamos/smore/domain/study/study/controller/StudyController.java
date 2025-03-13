package com.meossamos.smore.domain.study.study.controller;

import com.meossamos.smore.domain.study.study.dto.StudyDto;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;
    private final StudyMemberService studyMemberService;

    // 유저 스터디 목록 조회
    @GetMapping("/api/v1/user/studies")
    public ResponseEntity<List<StudyDto>> getUserStudies() {
        List<StudyDto> studies = studyMemberService.getStudiesByAuthenticatedUser();  // 서비스에서 스터디 목록 조회

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))  // 1시간 동안 캐시
                .body(studies);
    }

    // 스터디 정보 조회
    @GetMapping("/api/v1/study/{studyId}")
    public ResponseEntity<StudyDto> getStudyById(@PathVariable("studyId") Long studyId) {
        StudyDto studyDto = studyService.getStudyById(studyId);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))  // 1시간 동안 캐시
                .body(studyDto);
    }

    // 스터디 정보 수정
    @PutMapping("/api/v1/study/{study_Id}/introduction")
    public ResponseEntity<StudyDto> updateStudyIntroductions(
            @PathVariable("study_Id") Long studyId,
            @RequestBody StudyDto studyDto) {

        StudyDto updatedStudyDto = studyService.updateStudyIntroductions(studyId, studyDto);
        return new ResponseEntity<>(updatedStudyDto, HttpStatus.OK);
    }
}
