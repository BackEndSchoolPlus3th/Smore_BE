package com.meossamos.smore.domain.study.document.controller;

import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import com.meossamos.smore.domain.article.studyArticle.service.StudyArticleService;
import com.meossamos.smore.domain.study.document.dto.StudyDocumentDto;
import com.meossamos.smore.domain.study.document.entity.StudyDocument;
import com.meossamos.smore.domain.study.document.repository.StudyDocumentRepository;
import com.meossamos.smore.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/study/{studyId}")
public class StudyDocumentController {
    private final S3Service s3Service;
    private final StudyDocumentRepository studyDocumentRepository;
    private final StudyArticleService studyArticleService;

    /**
     * 문서함 조회
     * @param studyId 스터디 ID
     * @return 문서 리스트 (이름, URL)
     */
    @GetMapping("/documents")
    public List<StudyDocumentDto> getAllFiles(@PathVariable Long studyId) {
        return studyDocumentRepository.findByStudyId(studyId).stream()
                .map(doc -> new StudyDocumentDto(doc.getName(), doc.getUrl()))
                .collect(Collectors.toList());
    }

    /**
     * 게시물 내 파일 업로드
     * @param studyId 스터디 ID
     * @param articleId 게시물 ID
     * @param file 업로드할 파일
     * @return 업로드 결과 메시지
     * @throws IOException 파일 업로드 실패 시 예외 발생
     */
    @PostMapping("/articles/{articleId}/upload")
    public String articleUpload(@PathVariable Long studyId,
                                @PathVariable Long articleId,
                                @RequestParam("file") MultipartFile file) throws IOException {
        StudyArticle studyArticle = studyArticleService.findById(articleId);

        if (!studyArticle.getStudy().getId().equals(studyId)) {
            throw new IllegalArgumentException("잘못된 스터디입니다.");
        }

        String fileName = file.getOriginalFilename();
        String fileUrl = s3Service.uploadFile("articles", file);

        // 게시물 DB에 파일 URL 추가
        studyArticle.getAttachments().add(fileUrl);
        studyArticleService.save(studyArticle);

        // 문서함 DB에 파일 저장
        StudyDocument studyDocument = new StudyDocument();
        studyDocument.setName(fileName);
        studyDocument.setUrl(fileUrl);
        studyDocument.setType(file.getContentType());
        studyDocument.setStudy(studyArticle.getStudy());
        studyDocument.setStudyArticle(studyArticle);
        studyDocumentRepository.save(studyDocument);

        return "파일이 업로드되었습니다.";
    }

    /**
     * 파일 다운로드 (게시물 또는 문서함)
     * @param studyId 스터디 ID
     * @param articleId 게시물 ID (게시물 내 파일일 경우)
     * @param attachmentsId 문서함 파일 ID (문서함 파일일 경우)
     * @return 파일 다운로드 ResponseEntity
     * @throws IOException 파일 다운로드 실패 시 예외 발생
     */
    @GetMapping("/articles/{articleId}/download/{attachmentsId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long studyId,
                                               @PathVariable Long articleId,
                                               @PathVariable Long attachmentsId) throws IOException {
        StudyDocument studyDocument = studyDocumentRepository.findById(attachmentsId)
                .orElseThrow(() -> new RuntimeException("첨부파일을 찾을 수 없습니다."));

        if (!studyDocument.getStudy().getId().equals(studyId)) {
            throw new IllegalArgumentException("잘못된 스터디입니다.");
        }

        String fileName = Paths.get(studyDocument.getUrl()).getFileName().toString();
        ResponseInputStream<GetObjectResponse> responseInputStream = s3Service.downloadFile("articles/" + fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(responseInputStream.readAllBytes());
    }

    /**
     * 게시물 내 파일 삭제
     * @param studyId 스터디 ID
     * @param articleId 게시물 ID
     * @param fileName 삭제할 파일 이름
     * @return 삭제 결과 메시지
     */
    @PostMapping("/articles/{articleId}/delete")
    public String articleDelete(@PathVariable Long studyId,
                                @PathVariable Long articleId,
                                @RequestParam("fileName") String fileName) {
        s3Service.deleteFile("articles", fileName);
        return "파일이 삭제되었습니다.";
    }

    /**
     * 문서함 파일 삭제
     * @param studyId 스터디 ID
     * @param attachmentsId 삭제할 파일의 문서함 ID
     * @return 삭제 결과 메시지
     */
    @PostMapping("/documents/{attachmentsId}/delete")
    public String deleteDocument(@PathVariable Long studyId,
                                 @PathVariable Long attachmentsId) {
        StudyDocument studyDocument = studyDocumentRepository.findById(attachmentsId)
                .orElseThrow(() -> new RuntimeException("첨부파일을 찾을 수 없습니다."));

        if (!studyDocument.getStudy().getId().equals(studyId)) {
            throw new IllegalArgumentException("잘못된 스터디입니다.");
        }

        String fileName = Paths.get(studyDocument.getUrl()).getFileName().toString();
        s3Service.deleteFile("articles", fileName);
        studyDocumentRepository.deleteById(attachmentsId);

        return "파일이 삭제되었습니다.";
    }

    /**
     * 스터디 프로필 이미지 업로드
     */
    @PostMapping("/upload/profile/study")
    public String studyProfileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        return s3Service.uploadFile("study_profiles", file);
    }

    /**
     * 유저 프로필 이미지 업로드
     */
    @PostMapping("/upload/profile/user")
    public String userProfileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        return s3Service.uploadFile("user_profiles", file);
    }

    /**
     * 스터디 프로필 이미지 삭제
     */
    @PostMapping("/delete/profile/study")
    public String studyProfileDelete(@RequestParam("fileName") String fileName) {
        s3Service.deleteFile("study_profiles", fileName);
        return "파일이 삭제되었습니다.";
    }

    /**
     * 유저 프로필 이미지 삭제
     */
    @PostMapping("/delete/profile/user")
    public String userProfileDelete(@RequestParam("fileName") String fileName) {
        s3Service.deleteFile("user_profiles", fileName);
        return "파일이 삭제되었습니다.";
    }
}
