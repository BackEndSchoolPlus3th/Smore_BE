package com.meossamos.smore.domain.alarm.alarm.entity;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Alarm extends BaseEntity {

    @Column(nullable = true)
    private Long senderId;

    @Column(nullable = false)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver;

}
