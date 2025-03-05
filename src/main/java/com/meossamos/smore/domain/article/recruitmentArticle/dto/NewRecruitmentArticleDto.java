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
    private int maxMember;
    private LocalDate startDate;
    private LocalDate endDate;
    private String hashtags;
    private String thumbnailUrl;
    private String imageUrls;

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
