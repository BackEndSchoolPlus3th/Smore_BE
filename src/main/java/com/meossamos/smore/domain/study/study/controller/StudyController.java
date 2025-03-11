package com.meossamos.smore.domain.study.study.controller;

import com.meossamos.smore.domain.study.study.dto.StudyDto;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;
    private final StudyMemberService studyMemberService;

    // 유저 스터디 목록 조회
    @GetMapping("/api/v1/user/studies")
    public List<StudyDto> getUserStudies() {
        return studyMemberService.getStudiesByAuthenticatedUser();  // 서비스에서 스터디 목록 조회
    }

    // 스터디 정보 조회
    @GetMapping("/api/v1/study/{study_Id}")
    public StudyDto getStudyById(@PathVariable("study_Id") Long studyId) {
        return studyService.getStudyById(studyId);
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
