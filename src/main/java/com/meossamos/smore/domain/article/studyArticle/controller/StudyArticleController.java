package com.meossamos.smore.domain.article.studyArticle.controller;

import com.meossamos.smore.domain.article.studyArticle.dto.StudyArticleDto;
import com.meossamos.smore.domain.article.studyArticle.dto.request.StudyArticleCreateRequest;
import com.meossamos.smore.domain.article.studyArticle.service.StudyArticleService;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyArticleController {
    private final StudyArticleService studyArticleService;
    private final StudyMemberService studyMemberService;

    // 게시글 조회
    @GetMapping("/api/v1/study/{studyId}/articles")
    public List<StudyArticleDto> getArticlesByStudyId(@PathVariable("studyId") Long studyId) {
        return studyArticleService.getArticlesByStudyId(studyId);
    }

    // 게시글 상세 조회
    @GetMapping("/api/v1/study/{studyId}/articles/{articleId}")
    public ResponseEntity<StudyArticleDto> getStudyDetail(@PathVariable("articleId") Long articleId) {
        StudyArticleDto articleDto = studyArticleService.getStudyArticleById(articleId);
        return new ResponseEntity<>(articleDto, HttpStatus.OK);
    }

    // 게시물 검색
    @GetMapping("/api/v1/study/{studyId}/articles/search")
    public ResponseEntity<List<StudyArticleDto>> searchArticles(
            @PathVariable("studyId") Long studyId,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false) String content) {

        List<StudyArticleDto> searchResults = studyArticleService.searchArticles(studyId, title, content);
        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }

    // 게시글 작성
    @PostMapping("/api/v1/study/{studyId}/articles")
    public ResponseEntity<StudyArticleDto> createStudyArticle(
            @PathVariable Long studyId,
            @RequestParam String title,
            @RequestParam String content
    ) {
        // StudyArticleCreateRequest 객체 생성
        StudyArticleCreateRequest createRequest = StudyArticleCreateRequest.builder()
                .title(title)
                .content(content)
                .build();

        // 게시글 작성
        StudyArticleDto createdArticle = studyArticleService.createStudyArticle(studyId, createRequest);

        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    // 게시글 수정
    @PutMapping("/api/v1/study/{studyId}/articles/{articleId}")
    public ResponseEntity<StudyArticleDto> updateStudyArticle(
            @PathVariable Long articleId,
            @RequestBody StudyArticleDto updateRequest) {

        StudyArticleDto updatedArticle = studyArticleService.updateStudyArticle(articleId, updateRequest);
        return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping("/api/v1/study/{studyId}/articles/{articleId}")
    public ResponseEntity<String> deleteStudyArticle(
            @PathVariable Long articleId) {

        studyArticleService.deleteStudyArticle(articleId);
        return new ResponseEntity<>("게시글이 삭제되었습니다.", HttpStatus.OK);
    }
}
