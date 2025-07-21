package com.meossamos.smore.domain.chat.webMedia.model.message;

import com.meossamos.smore.domain.chat.webMedia.model.ErrorCode;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponseMessage {
    private ErrorCode errorCode;
    private String message;

}
