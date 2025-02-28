package com.meossamos.smore.domain.article.recruitmentArticleHashTag.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "es_recruitment_article_hash_tag", createIndex = true)
@Setting(settingPath = "elasticsearch/settings.json")
@Mapping(mappingPath = "elasticsearch/mappings.json")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentArticleHashTagDoc {
    @Id
    private String id;

    @Field(name = "hash_tag", type = FieldType.Keyword)
    private String hashTag;

    @Field(name = "recruitment_article_id", type = FieldType.Keyword)
    private Long recruitmentArticleId;
}
