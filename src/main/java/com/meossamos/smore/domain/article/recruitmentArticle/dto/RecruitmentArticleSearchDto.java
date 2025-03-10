package com.meossamos.smore.domain.article.recruitmentArticle.dto;

import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class RecruitmentArticleSearchDto {
    private int page = 1;
    private int size = 12;
    private String title;
    private String content;
    private String introduction;
    private String hashTags;
    private String region;
    private boolean isCustomSearch;

    public List<String> getTitleList() {
        return title == null || title.trim().isEmpty()
                ? Collections.emptyList()
                : Arrays.asList(title.split(","));
    }

    public List<String> getContentList() {
        return content == null || content.trim().isEmpty()
                ? Collections.emptyList()
                : Arrays.asList(content.split(","));
    }

    public List<String> getIntroductionList() {
        return introduction == null || introduction.trim().isEmpty()
                ? Collections.emptyList()
                : Arrays.asList(introduction.split(","));
    }

    public List<String> getHashTagsList() {
        return hashTags == null || hashTags.trim().isEmpty()
                ? Collections.emptyList()
                : Arrays.asList(hashTags.split(","));
    }

    public List<String> getRegionList() {
        return region == null || region.trim().isEmpty()
                ? Collections.emptyList()
                : Arrays.asList(region.split(","));
    }
}
