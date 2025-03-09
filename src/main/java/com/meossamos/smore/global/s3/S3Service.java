package com.meossamos.smore.global.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    private final S3Config s3Config;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name.public}")
    private String bucketName;


    /**
     * S3에 파일 업로드를 위한 프리사인 URL을 생성한다.
     *
     * @param fileName    S3에 저장될 파일 이름(키)
     * @param contentType 파일의 MIME 타입 (예: image/png)
     * @return 유효 시간(예: 10분) 동안 사용할 수 있는 프리사인 URL 문자열
     */
    public String generatePresignedUrl(String fileName, String contentType) {
        // S3 PutObjectRequest 생성: 버킷, 파일 키, 콘텐츠 타입 설정
        System.out.println("bucket name: " + bucketName);
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .build();

        // 프리사인 요청 설정: URL의 유효 시간을 10분으로 설정
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(objectRequest)
                .build();

        // 프리사인 URL 생성
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        // 생성된 URL을 문자열로 반환
        return presignedRequest.url().toString();
    }

    /**
     * S3에서 특정 파일의 URL을 생성하여 반환
     * @param fileName 파일 이름 (디렉터리 포함)
     * @return S3 파일 URL
     */
    public String getS3FileUrl(String fileName) {
        return "https://" + bucketName + ".s3." + s3Config.getRegion() + ".amazonaws.com/" + fileName;
    }

    /**
     * S3에 파일 업로드
     * @param directory 업로드할 디렉터리 (예: "documents")
     * @param file 업로드할 파일
     * @return 업로드된 파일의 S3 URL
     * @throws IOException 파일 업로드 실패 시 예외 발생
     */
    public String uploadFile(String directory, MultipartFile file) throws IOException {
        String key = directory + "/" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return getS3FileUrl(key);
    }

    /**
     * S3에서 파일 다운로드
     * @param key 다운로드할 파일의 키 (경로 포함)
     * @return 파일의 ResponseInputStream
     */
    public ResponseInputStream<GetObjectResponse> downloadFile(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return s3Client.getObject(getObjectRequest);
    }

    /**
     * S3에서 지정된 디렉터리 내의 파일을 삭제
     *
     * @param directory 삭제할 파일이 있는 디렉터리 (예: "studies/123/images")
     * @param fileName  삭제할 파일의 고유 이름 (예: "uniqueFileName-원본파일명.jpg")
     */
    public void deleteFile(String directory, String fileName) {
        // 디렉터리가 "/"로 끝나지 않으면 "/"를 추가하여 파일 키를 구성
        String key = directory.endsWith("/") ? directory + fileName : directory + "/" + fileName;

        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(deleteRequest);
    }

    /**
     * S3에서 특정 파일이 존재하는지 확인
     * @param key 확인할 파일의 키 (경로 포함)
     * @return 파일 존재 여부 (true: 존재함, false: 존재하지 않음)
     */
    public boolean doesFileExist(String key) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    /**
     * S3에서 파일 메타데이터 조회 (크기, 마지막 수정 시간 등)
     * @param key 파일의 키 (경로 포함)
     * @return 파일 메타데이터 정보 (HeadObjectResponse)
     */
    public HeadObjectResponse getFileMetadata(String key) {
        return s3Client.headObject(HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
    }

    /**
     * S3에서 특정 파일의 Presigned URL (서명된 URL) 생성
     * @param key 다운로드할 파일의 키 (경로 포함)
     * @param expirationMinutes Presigned URL의 유효 시간 (분 단위)
     * @return 생성된 Presigned URL
     */
    public URL generatePresignedUrl(String key, int expirationMinutes) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(r -> r
                .signatureDuration(Duration.ofMinutes(expirationMinutes))
                .getObjectRequest(getObjectRequest));

        return presignedRequest.url();
    }

    /**
     * S3에서 특정 디렉터리 내 파일 목록 조회
     * @param directory 조회할 디렉터리 (예: "documents/")
     * @return 해당 디렉터리 내 파일 목록 (키 리스트)
     */
    public List<String> listFiles(String directory) {
        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(directory)
                .build();

        return s3Client.listObjectsV2(listObjectsRequest).contents().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }

    /**
     * S3 내에서 파일 복사
     * @param sourceKey 원본 파일 키
     * @param destinationKey 복사할 대상 키
     */
    public void copyFile(String sourceKey, String destinationKey) {
        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                .sourceBucket(bucketName)
                .destinationBucket(bucketName)
                .sourceKey(sourceKey)
                .destinationKey(destinationKey)
                .build();

        s3Client.copyObject(copyObjectRequest);
    }

    /**
     * S3 내에서 파일 이동 (복사 후 원본 삭제)
     * @param sourceKey 원본 파일 키
     * @param destinationKey 이동할 대상 키
     */
    public void moveFile(String sourceKey, String destinationKey) {
        copyFile(sourceKey, destinationKey);
        deleteFile("", sourceKey);
    }
}
