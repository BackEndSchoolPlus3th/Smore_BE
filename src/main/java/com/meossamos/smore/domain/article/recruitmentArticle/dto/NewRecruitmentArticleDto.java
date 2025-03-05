package com.meossamos.smore.domain.article.recruitmentArticle.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class NewRecruitmentArticleDto {
    private String title;
    private String content;
    private String introduction;
    private String region;
    // 모집 시작일과 종료일은 문자열로 받아서 이후 LocalDate로 변환하거나,
    // 혹은 프론트엔드에서 ISO 형식(yyyy-MM-dd)으로 보내면 바로 LocalDate로 매핑할 수 있습니다.
    private LocalDate startDate;
    private LocalDate endDate;
    /**
     * 해시태그는 프론트엔드에서 JSON 문자열(ex. '["tag1", "tag2"]')로 전달합니다.
     * 이후 getHashtagList()를 통해 List<String>로 변환할 수 있습니다.
     */
    private String hashtags;
    private String thumbnailUrl;

    // JSON 문자열로 받은 hashtags를 List<String>으로 변환하는 헬퍼 메서드
    public List<String> getHashtagList() {
        if (hashtags == null || hashtags.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(hashtags, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            // 변환 실패 시 빈 리스트 반환 또는 로그 처리
            return new ArrayList<>();
        }
    }
}
