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
    private List<String> hashtags;
    private String thumbnailUrl;
    private String imageUrls;

}
