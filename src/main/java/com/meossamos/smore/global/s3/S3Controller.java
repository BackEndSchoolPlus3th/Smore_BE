package com.meossamos.smore.global.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
public class S3Controller {
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

    /**
     * 파일 삭제 엔드포인트
     * 클라이언트는 삭제할 파일의 디렉터리와 파일 이름을 쿼리 파라미터로 전달합니다.
     *
     * 예시 요청: DELETE /api/v1/s3/delete?directory=studies/123/images&fileName=uniqueFileName-원본파일명.jpg
     *
     * @param directory 삭제할 파일이 있는 디렉터리 (예: "studies/123/images")
     * @param fileName  삭제할 파일의 고유 이름
     * @return 삭제 성공 시 204 No Content, 실패 시 에러 메시지와 함께 500 상태 코드 반환
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFile(
            @RequestParam("directory") String directory,
            @RequestParam("fileName") String fileName) {
        try {
            s3Service.deleteFile(directory, fileName);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파일 삭제에 실패했습니다.");
        }
    }
}
