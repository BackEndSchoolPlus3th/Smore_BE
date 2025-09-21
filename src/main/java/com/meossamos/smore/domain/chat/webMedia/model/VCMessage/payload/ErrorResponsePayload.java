package com.meossamos.smore.domain.chat.webMedia.model.VCMessage.payload;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponsePayload {
    public String errorCode;
    public String message;
}
