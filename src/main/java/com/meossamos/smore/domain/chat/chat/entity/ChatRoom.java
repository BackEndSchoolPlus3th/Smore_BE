package com.meossamos.smore.domain.chat.chat.entity;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.global.jpa.BaseEntity;
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
public class ChatRoom extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member1;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member2;
}
