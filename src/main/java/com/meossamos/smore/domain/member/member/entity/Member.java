package com.meossamos.smore.domain.member.member.entity;

import co.elastic.clients.util.DateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meossamos.smore.domain.home.article.entity.Article;
import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Member extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = true)
    private Date birthdate;

    @Column(nullable = true)
    private String region;

    @Column(nullable = true)
    private String hashTags;

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Article> articleList = new ArrayList<>();
}
