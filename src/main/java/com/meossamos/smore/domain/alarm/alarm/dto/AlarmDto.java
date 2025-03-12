package com.meossamos.smore.domain.alarm.alarm.dto;

import com.meossamos.smore.domain.member.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmDto {

    private Long id;
    private Long senderId;

    private Long receiverId;
    private String message;

    private Long studyId;

    private String eventName;

    private Long recruitmentId;

}
