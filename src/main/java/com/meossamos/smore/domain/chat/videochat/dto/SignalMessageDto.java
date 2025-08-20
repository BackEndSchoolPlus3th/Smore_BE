package com.meossamos.smore.domain.chat.videochat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignalMessageDto {
    private String type;
    private String message;
}
