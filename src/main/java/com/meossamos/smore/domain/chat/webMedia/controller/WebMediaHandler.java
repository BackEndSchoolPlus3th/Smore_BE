package com.meossamos.smore.domain.chat.webMedia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meossamos.smore.domain.chat.webMedia.model.MessageType;
import com.meossamos.smore.domain.chat.webMedia.model.StringMessageContainer;
import com.meossamos.smore.domain.chat.webMedia.service.MessageSender;
import com.meossamos.smore.domain.chat.webMedia.service.LegacyRoomAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WebMediaHandler  extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final MessageSender messageSender;

    //동시성 문제
    private final Object lockObj;
    private final Map<String, LegacyRoomAgent> agentMap;

    public WebMediaHandler(ObjectMapper objectMapper, MessageSender messagSender){
        this.objectMapper = objectMapper;
        this.messageSender = messagSender;

        this.lockObj = new Object();
        this.agentMap = new HashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        log.debug("Connection established : sessionId={}", session.getId());


    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws  Exception{
        final String payload = message.getPayload();

        try {
            final StringMessageContainer messageContainer = objectMapper.readValue(payload, StringMessageContainer.class);
            LegacyRoomAgent agent = null;

            if(MessageType.JoinRequest.equals(messageContainer.getType())){
                final  String roomId = messageContainer.getRoomId();
                synchronized (lockObj){
                    if(agentMap.containsKey(roomId)){
                        agent = agentMap.get(roomId);
                    } else {
                        agent = new LegacyRoomAgent(objectMapper, messageSender, roomId);
                        agentMap.put(roomId, agent);
                    }
                }
            } else {
                synchronized (lockObj) {
                    agent = agentMap.get(messageContainer.getRoomId());
                }
            }
            agent.handleMessage(session, messageContainer.getMessageId(), messageContainer.getType(), messageContainer.getMessage());
        }catch (Exception e){
            log.debug("handleTextMessage error", e);

            session.close(new CloseStatus(3000,"알 수 없는 에러"));
        }
    }
}
