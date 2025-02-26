package com.meossamos.smore.domain.article.studyArticle.controller;

import com.meossamos.smore.domain.article.studyArticle.dto.StudyArticleDto;
import com.meossamos.smore.domain.article.studyArticle.dto.request.StudyArticleCreateRequest;
import com.meossamos.smore.domain.article.studyArticle.service.StudyArticleService;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.dto.StudyDto;
import com.meossamos.smore.domain.study.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyArticleController {
    private final StudyArticleService studyArticleService;

    @GetMapping("/api/study/{study_Id}/articles")
    public List<StudyArticleDto> getArticlesByStudyId(@PathVariable("study_Id") Long studyId) {
        return studyArticleService.getArticlesByStudyId(studyId);
    }

    @GetMapping("/api/study/{study_Id}/articles/{article_Id}")
    public ResponseEntity<StudyArticleDto> getStudyDetail(@PathVariable("article_Id") Long articleId) {
        StudyArticleDto articleDto = studyArticleService.getStudyArticleById(articleId);
        return new ResponseEntity<>(articleDto, HttpStatus.OK);
    }

    @PostMapping("/api/study/{study_Id}/articles")
    public ResponseEntity<StudyArticleDto> createStudyArticle(
            @PathVariable("study_Id") Long studyId,
            @RequestBody StudyArticleCreateRequest createRequest,
            @AuthenticationPrincipal Member member) {

        StudyArticleDto createdArticle = studyArticleService.createStudyArticle(studyId, createRequest, member);

        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }
}
