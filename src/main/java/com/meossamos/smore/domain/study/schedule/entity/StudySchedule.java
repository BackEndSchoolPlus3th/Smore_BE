package com.meossamos.smore.domain.study.schedule.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@NamedQuery(
        name = "StudySchedule.findByStudy_Id",
        query = "SELECT s FROM StudySchedule s WHERE s.study.id = :studyId"
)
public class StudySchedule extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = true)
    private String content;

    @Column(nullable = false)
    private boolean allDay;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    @JsonIgnore
    private Study study;
}
