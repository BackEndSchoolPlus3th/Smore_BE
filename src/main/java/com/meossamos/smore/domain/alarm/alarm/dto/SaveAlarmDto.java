package com.meossamos.smore.domain.alarm.alarm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveAlarmDto {
    private Long senderId;

    private String message;

    private Long receiverId;

    private Long studyId;

    private String eventName;

    private Long recruitmentId;

}
