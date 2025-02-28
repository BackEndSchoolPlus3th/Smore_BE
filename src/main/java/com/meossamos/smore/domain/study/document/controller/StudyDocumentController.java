package com.meossamos.smore.domain.study.document.controller;

import com.meossamos.smore.domain.article.studyArticle.entity.StudyArticle;
import com.meossamos.smore.domain.article.studyArticle.repository.StudyArticleRepository;
import com.meossamos.smore.domain.study.document.dto.StudyDocumentDto;
import com.meossamos.smore.domain.study.document.entity.StudyDocument;
import com.meossamos.smore.domain.study.document.repository.StudyDocumentRepository;
import com.meossamos.smore.domain.study.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StudyDocumentController {
    private final S3Client s3Client;
    private static final String BUCKET_NAME = "smore-bucket-smore-s3-1";
    private static final String REGION = "ap-northeast-2";

    private final StudyArticleRepository studyArticleRepository;
    private final StudyDocumentRepository studyDocumentRepository;
    private final StudyRepository studyRepository;

    public static String getS3FileUrl(String fileName) {
        return "https://" + BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/" + fileName;
    }

    // 파일 업로드
    private String uploadFile(String directory, MultipartFile file) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(directory + "/" + file.getOriginalFilename())
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return "Uploaded Success";
    }

    // 파일 삭제
    private String deleteFile(String directory, String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(directory + "/" + fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        return "파일이 삭제되었습니다.";
    }

    // 문서함 조회
    @GetMapping("/api/study/{study_Id}/documents")
    public List<StudyDocumentDto> getAllFiles(@PathVariable String study_Id) {
        Long studyId = Long.parseLong(study_Id);

        // study_Id에 해당하는 문서함의 파일 URL 목록 조회
        List<StudyDocument> studyDocuments = studyDocumentRepository.findByStudyId(studyId);

        // 파일 name, url 추출하여 반환
        List<StudyDocumentDto> fileList = studyDocuments.stream()
                .map(doc -> new StudyDocumentDto(doc.getName(), doc.getUrl()))
                .collect(Collectors.toList());

        return fileList;
    }

    // 파일 다운로드 (게시물 내 혹은 문서함)
    @GetMapping("/api/study/{study_Id}/articles/{articleId}/download/{attachmentsId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long study_Id, @PathVariable Long articleId, @PathVariable Long attachmentsId) throws IOException {
        // 게시물 내 파일 다운로드
        if (attachmentsId == null) {
            // 파일 찾기
            StudyArticle studyArticle = studyArticleRepository.findById(articleId)
                    .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

            // study_Id 체크
            if (!studyArticle.getStudy().getId().equals(study_Id)) {
                throw new RuntimeException("잘못된 스터디입니다.");
            }

            // 파일 URL을 DB에서 찾기
            String fileUrl = studyArticle.getAttachments().stream()
                    .filter(url -> url.contains("articles/"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("첨부파일을 찾을 수 없습니다."));

            // URL에서 파일 이름 추출
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            // S3에서 파일 다운로드
            return downloadFromS3(fileName);
        }

        // 문서함에서 파일 다운로드
        else {
            // 문서함에서 파일 조회
            StudyDocument studyDocument = studyDocumentRepository.findById(attachmentsId)
                    .orElseThrow(() -> new RuntimeException("첨부파일을 찾을 수 없습니다."));

            // study_Id 체크
            if (!studyDocument.getStudy().getId().equals(study_Id)) {
                throw new RuntimeException("잘못된 스터디입니다.");
            }

            String fileName = studyDocument.getName();

            // S3에서 파일 다운로드
            return downloadFromS3(fileName);
        }
    }

    // S3에서 파일 다운로드 처리
    private ResponseEntity<byte[]> downloadFromS3(String fileName) throws IOException {
        // S3 파일 조회
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key("articles/" + fileName)
                .build();

        // S3에서 파일을 다운로드
        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

        // 바이트 배열
        byte[] fileBytes = s3Object.readAllBytes();

        // 파일 다운로드 응답
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(fileBytes);
    }

    // 게시물 내 파일 업로드
    @PostMapping("/api/study/{study_Id}/articles/{articleId}/upload")
    @ResponseBody
    public String articleUpload(@PathVariable Long articleId, @RequestParam("file") MultipartFile file) throws IOException {
        StudyArticle studyArticle = studyArticleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));

        // S3에 파일 업로드
        String fileName = file.getOriginalFilename();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key("articles/" + fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // S3 URL
        String fileUrl = getS3FileUrl("articles/" + fileName);

        // 게시물 DB에 파일 URL 추가
        studyArticle.getAttachments().add(fileUrl);
        studyArticleRepository.save(studyArticle);

        StudyDocument studyDocument = new StudyDocument();
        studyDocument.setName(fileName);
        studyDocument.setUrl(fileUrl);
        studyDocument.setType(file.getContentType());
        studyDocument.setStudy(studyArticle.getStudy());
        studyDocument.setStudyArticle(studyArticle);

        // 문서함DB에 파일 저장
        studyDocumentRepository.save(studyDocument);

        return "파일이 업로드되었습니다.";
    }

    // 스터디 프로필 이미지 업로드
    @PostMapping("/api/study/{study_Id}/upload/profile/study")
    @ResponseBody
    public String studyProfileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        return uploadFile("study_profiles", file);
    }

    // 유저 프로필 이미지 업로드
    @PostMapping("/api/study/{study_Id}/upload/profile/user")
    @ResponseBody
    public String userProfileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        return uploadFile("user_profiles", file);
    }

    // 게시물 내 파일 삭제
    @PostMapping("/api/study/{study_Id}/articles/{articleId}/delete")
    @ResponseBody
    public String articleDelete(String fileName) {
        return deleteFile("articles", fileName);
    }

    // 스터디 프로필 이미지 삭제
    @PostMapping("/api/study/{study_Id}/delete/profile/study")
    @ResponseBody
    public String studyProfileDelete(String fileName) {
        return deleteFile("study_profiles", fileName);
    }

    // 유저 프로필 이미지 삭제
    @PostMapping("/api/study/{study_Id}/delete/profile/user")
    @ResponseBody
    public String userProfileDelete(String fileName) {
        return deleteFile("user_profiles", fileName);
    }
}
