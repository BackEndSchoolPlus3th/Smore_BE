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

    // 게시글 조회
    @GetMapping("/api/study/{study_Id}/articles")
    public List<StudyArticleDto> getArticlesByStudyId(@PathVariable("study_Id") Long studyId) {
        return studyArticleService.getArticlesByStudyId(studyId);
    }

    // 게시글 상세 조회
    @GetMapping("/api/study/{study_Id}/articles/{article_Id}")
    public ResponseEntity<StudyArticleDto> getStudyDetail(@PathVariable("article_Id") Long articleId) {
        StudyArticleDto articleDto = studyArticleService.getStudyArticleById(articleId);
        return new ResponseEntity<>(articleDto, HttpStatus.OK);
    }

    // 게시물 검색
    @GetMapping("/api/study/{study_Id}/articles/search")
    public ResponseEntity<List<StudyArticleDto>> searchArticles(
            @PathVariable("study_Id") Long studyId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content) {

        List<StudyArticleDto> searchResults = studyArticleService.searchArticles(studyId, title, content);
        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }

    // 게시글 작성
    @PostMapping("/api/study/{study_Id}/articles")
    public ResponseEntity<StudyArticleDto> createStudyArticle(
            @PathVariable("study_Id") Long studyId,
            @RequestBody StudyArticleCreateRequest createRequest,
            @AuthenticationPrincipal Member member) {

        StudyArticleDto createdArticle = studyArticleService.createStudyArticle(studyId, createRequest, member);

        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    // 게시글 수정
    @PutMapping("/api/study/{study_Id}/articles/{article_Id}")
    public ResponseEntity<StudyArticleDto> updateStudyArticle(
            @PathVariable("study_Id") Long studyId,
            @PathVariable("article_Id") Long articleId,
            @RequestBody StudyArticleDto updateRequest) {

        StudyArticleDto updatedArticle = studyArticleService.updateStudyArticle(articleId, updateRequest);
        return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping("/api/study/{study_Id}/articles/{article_Id}")
    public ResponseEntity<Void> deleteStudyArticle(
            @PathVariable("study_Id") Long studyId,
            @PathVariable("article_Id") Long articleId) {

        studyArticleService.deleteStudyArticle(articleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
    }
}
