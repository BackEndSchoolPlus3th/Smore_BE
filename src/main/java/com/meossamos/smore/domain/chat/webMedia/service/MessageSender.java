package com.meossamos.smore.domain.chat.webMedia.service;

import com.meossamos.smore.domain.chat.webMedia.model.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meossamos.smore.domain.chat.webMedia.model.ObjectMessageContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


@Service
public class MessageSender {
    private static final String FromValue="webmedia-ws";
    private static final String AllValue = "all";
    private static final String NoTransactionMessageId = "none";

    private ObjectMapper objectMapper;

    @Autowired
    public MessageSender(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    // 요청-응답 구조의 메시지(특정 상대에게)
    public void sendTransactionMessage(WebSocketSession session,
                                       String roomId, String to,
                                       String messageId, MessageType type,
                                       Object message) throws Exception {
        final ObjectMessageContainer container = ObjectMessageContainer.builder()
                .roomId(roomId)
                .from(FromValue)
                .to(to)
                .type(type)
                .messageId(messageId)
                .message(message)
                .build();

        final String containerString = objectMapper.writeValueAsString(container);
        final TextMessage textMessage = new TextMessage(containerString);

        session.sendMessage(textMessage);
    }

    //상태 전파용 알림 메시지 (이벤트 알림, 모두에게)
    public void sendEventMessage(WebSocketSession session,
                                 String roomId, MessageType type,
                                 Object message) throws Exception {
        final ObjectMessageContainer container = ObjectMessageContainer.builder()
                .roomId(roomId)
                .from(roomId)
                .to(AllValue)
                .type(type)
                .messageId(NoTransactionMessageId)
                .message(message)
                .build();

        final String containerString = objectMapper.writeValueAsString(container);
        final TextMessage textMessage = new TextMessage(containerString);

        session.sendMessage(textMessage);
    }



}
