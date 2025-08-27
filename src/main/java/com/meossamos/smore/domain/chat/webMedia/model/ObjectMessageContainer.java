package com.meossamos.smore.domain.chat.webMedia.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
// 서버 -> 클라이언트
public class ObjectMessageContainer {
    // 헤더
    private String roomId;
    private String from;
    private String to;
    private MessageType type;
    private String messageId;

    private Object message;

}
