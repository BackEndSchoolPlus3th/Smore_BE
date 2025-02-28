package com.meossamos.smore.domain.article.recruitmentArticle.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "es_recruitment_article", createIndex = true)
@Setting(settingPath = "elasticsearch/settings.json")
@Mapping(mappingPath = "elasticsearch/mappings.json")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentArticleDoc {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Date, name = "created_date")
    private Date created_date;

    @Field(type = FieldType.Date, name = "end_date")
    private Date end_date;

    // 해시태그가 콤마로 구분된 문자열로 저장되는 경우 String으로 사용
    @Field(type = FieldType.Text, name = "hash_tags")
    private String hash_tags;

    @Field(type = FieldType.Text, name = "image_urls")
    private String image_urls;

    @Field(type = FieldType.Boolean, name = "is_recruiting")
    private Boolean is_recruiting;

    @Field(type = FieldType.Long, name = "max_member")
    private Long max_member;

    @Field(type = FieldType.Long, name = "member_id")
    private Long member_id;

    @Field(type = FieldType.Text)
    private String region;

    @Field(type = FieldType.Date, name = "start_date")
    private Date start_date;

    @Field(type = FieldType.Long, name = "study_id")
    private Long study_id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String type;

    @Field(type = FieldType.Date, name = "updated_date")
    private Date updated_date;
}