package com.meossamos.smore.domain.chat.videochat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignalMessageDto {
    private String type;         // join, offer, answer, ice 등
    private String roomId;       // 대상 방
    private String sender;       // 보낸 사용자 ID
    private Object data;         // offer/answer/ice 데이터
}
