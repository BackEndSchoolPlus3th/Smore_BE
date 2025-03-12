package com.meossamos.smore.domain.article.studyArticle.controller;

import com.meossamos.smore.domain.article.studyArticle.dto.StudyArticleDto;
import com.meossamos.smore.domain.article.studyArticle.dto.request.StudyArticleCreateRequest;
import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import com.meossamos.smore.domain.article.studyArticle.repository.StudyArticleRepository;
import com.meossamos.smore.domain.article.studyArticle.service.StudyArticleService;
import com.meossamos.smore.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StudyArticleController {
    private final StudyArticleService studyArticleService;
    private final S3Service s3Service;
    private final StudyArticleRepository studyArticleRepository;

    @Value("${aws.s3.bucket-name.public}")
    private String bucketName;

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
            @RequestParam String content,
            @RequestParam(required = false) String file
    ) throws IOException {
        // StudyArticleCreateRequest 객체 생성
        StudyArticleCreateRequest createRequest = StudyArticleCreateRequest.builder()
                .title(title)
                .content(content)
                .build();

        // 게시글 작성
        StudyArticleDto createdArticle = studyArticleService.createStudyArticle(studyId, createRequest, file);

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

    // 파일 다운
    @GetMapping("/api/v1/study/download/{key}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String key) throws IOException {
        // S3에서 파일을 다운로드
        ResponseInputStream<GetObjectResponse> s3Object = s3Service.downloadFile(key);

        // 파일의 내용을 바이트 배열로 변환
        byte[] content = s3Object.readAllBytes();

        // 다운로드할 파일 이름 설정
        String fileName = key.substring(key.lastIndexOf("/") + 1);

        // HTTP 응답으로 파일을 스트리밍
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(content);
    }

    // 문서함 조회
    @GetMapping("/api/v1/study/document/{studyId}")
    public List<String> getImagesByStudyId(@PathVariable Long studyId) {
        List<StudyArticle> articles = studyArticleRepository.findByStudyId(
                studyId,
                Sort.by(Sort.Order.desc("createdDate")) // createdDate 기준으로 내림차순 정렬
        );

        // 각 StudyArticle에서 imageUrls을 추출하여 반환
        return articles.stream()
                .map(StudyArticle::getImageUrls)  // imageUrls을 가져옵니다
                .filter(imageUrl -> imageUrl != null && !imageUrl.isEmpty())  // 빈 값은 제외
                .collect(Collectors.toList());
    }
}
