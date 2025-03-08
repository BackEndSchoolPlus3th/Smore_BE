package com.meossamos.smore.domain.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final S3Client s3Client;
    private static final String PUBLIC_BUCKET_NAME = "smore-dev-bucket-public-1";
    private static final String PRIVATE_BUCKET_NAME = "smore-dev-bucket-private-1";
    private static final String REGION = "ap-northeast-2";
    private static final String IMG_DIR_NAME = "img1";

    public static String getS3FileUrl(String fileName) {
        return "https://" + PUBLIC_BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/" + fileName;
    }

    @GetMapping("/")
    public List<String> home() {
        List<Bucket> bucketList = s3Client.listBuckets().buckets();
        return bucketList.stream().map(Bucket::name).collect(Collectors.toList());
    }

    @GetMapping("/upload")
    public String upload() {
        return """
                <form action="/test/upload" method="post" enctype="multipart/form-data">
                    <input type="file" name="file" accept="image/*">
                    <input type="submit" value="Upload">
                </form>
                """;
    }

    @PostMapping("/upload")
    @ResponseBody
    public String handleFileUpload(MultipartFile file) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(PUBLIC_BUCKET_NAME)
                .key(IMG_DIR_NAME + "/" + file.getOriginalFilename())
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return"""
                <img src="%s">
                <hr>
                <div>업로드 완료</div>
                """.formatted(getS3FileUrl(IMG_DIR_NAME + "/" + file.getOriginalFilename()));
    }

    @GetMapping("/deleteFile")
    public String showDeleteFile() {
        return """
                <form action="/test/deleteFile" method="post">
                    <input type="text" name="fileName">
                    <input type="submit" value="delete">
                </form>
                """;
    }

    @PostMapping("/deleteFile")
    @ResponseBody
    public String deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(PUBLIC_BUCKET_NAME)
                .key(IMG_DIR_NAME + "/" + fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
        return "파일이 삭제되었습니다.";
    }
}
