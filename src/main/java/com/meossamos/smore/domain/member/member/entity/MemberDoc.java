package com.meossamos.smore.domain.member.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "es_member", createIndex = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Setting(settingPath = "elasticsearch/settings.json")
@Mapping(mappingPath = "elasticsearch/mappings.json")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDoc {
    @Id
    private String id;
    private String name;
    private String email;
    private String profileImageUrl;
}
