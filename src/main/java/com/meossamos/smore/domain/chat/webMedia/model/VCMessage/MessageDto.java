package com.meossamos.smore.domain.chat.webMedia.model.VCMessage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.meossamos.smore.domain.chat.webMedia.model.VCMessage.payload.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDto<T> {
    public String messageId;
    public String roomId;
    public String userId;

    public LocalDateTime sentAt;
    public MessageType type;
    public T payload;
}
