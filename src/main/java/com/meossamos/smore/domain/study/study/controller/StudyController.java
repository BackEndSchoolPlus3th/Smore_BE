package com.meossamos.smore.domain.study.study.controller;

import com.meossamos.smore.domain.article.studyArticle.dto.StudyArticleDto;
import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.dto.StudyDto;
import com.meossamos.smore.domain.study.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;

    @GetMapping("/api/study/my-studies")
    public ResponseEntity<List<StudyDto>> getStudiesForMember(@AuthenticationPrincipal Member member) {
        List<StudyDto> studyDtos = studyService.getStudiesForMember(member);
        return new ResponseEntity<>(studyDtos, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/api/study/{study_Id}")
    public StudyDto getStudyById(@PathVariable("study_Id") Long studyId) {
        return studyService.getStudyById(studyId);
    }
}
