package com.meossamos.smore.domain.chat.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequestDto {
    private String roomId;
    private String senderId;
    private String message;
//    private LocalDateTime timestamp;
    private String attachment;

}
