package com.meossamos.smore.domain.chat.webMedia.model.VCMessage;

import com.meossamos.smore.domain.chat.webMedia.model.VCMessage.payload.type;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MessageDto<T> {
    public String messageId;
    public String roomId;
    public String userId;

    public LocalDateTime sentAt;
    public type type;
    public T payload;
}
