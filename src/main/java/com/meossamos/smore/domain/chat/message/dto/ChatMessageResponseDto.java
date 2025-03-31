package com.meossamos.smore.domain.chat.message.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseDto {
    private String messageId;
    private String roomId;
    private String senderId;
    private String message;
    private String attachment;
    private LocalDateTime timestamp;

}
