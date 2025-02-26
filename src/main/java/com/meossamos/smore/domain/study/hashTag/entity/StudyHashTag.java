package com.meossamos.smore.domain.study.hashTag.entity;

import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StudyHashTag extends BaseEntity {
    @Column(nullable = false)
    private String hashTag;

    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;
}
