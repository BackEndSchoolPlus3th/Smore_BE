package com.meossamos.smore.domain.test.controller;

import com.meossamos.smore.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
public class TestController {
    // S3 관련 비즈니스 로직을 처리하는 서비스 주입
    private final S3Service s3Service;

    /**
     * 프리사인 URL을 생성하여 반환하는 엔드포인트
     *
     * 예시 요청: GET /api/s3/presign?fileName=myimage.png&contentType=image/png
     *
     * @param fileName    업로드할 파일의 이름
     * @param contentType 업로드할 파일의 MIME 타입
     * @return 프리사인 URL을 포함한 JSON 응답
     */
    @GetMapping("/presign")
    public ResponseEntity<Map<String, String>> getPresignedUrl(
            @RequestParam("fileName") String fileName,
            @RequestParam("contentType") String contentType
    ) {
        // 서비스 호출하여 프리사인 URL 생성
        String presignedUrl = s3Service.generatePresignedUrl(fileName, contentType);

        // 응답 결과를 Map으로 생성하여 반환
        Map<String, String> response = new HashMap<>();
        response.put("url", presignedUrl);
        return ResponseEntity.ok(response);
    }
}
