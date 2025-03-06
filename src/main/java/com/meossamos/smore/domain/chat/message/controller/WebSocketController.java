package com.meossamos.smore.domain.chat.message.controller;

import com.meossamos.smore.domain.chat.message.dto.ChatMessageRequestDto;
import com.meossamos.smore.domain.chat.message.dto.ChatMessageResponseDto;
import com.meossamos.smore.domain.chat.message.entity.ChatMessage;
import com.meossamos.smore.domain.chat.message.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate; // 메시지 전송 도구
    /**
     * 클라이언트가 "/app/chat.sendMessage"로 메시지를 전송하면
     * 이 메서드가 실행되어 메시지를 저장한 후 "/topic/chatroom"로 브로드캐스트함.
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessageRequestDto messageDto) {
        // 1. 메시지 저장
        ChatMessage savedMessage = chatMessageService.saveChatMessage(
                messageDto.getRoomId(),
                messageDto.getSenderId(),
                messageDto.getMessage(),
                messageDto.getAttachment()
        );

        // 2. 저장된 엔티티를 Response DTO로 변환하여 응답
        ChatMessageResponseDto response = ChatMessageResponseDto.builder()
                .messageId(savedMessage.getId())
                .roomId(savedMessage.getRoomId())
                .senderId(savedMessage.getSenderId())
                .message(savedMessage.getMessage())
                .attachment(savedMessage.getAttachment())
                .timestamp(savedMessage.getCreatedDate() != null ? savedMessage.getCreatedDate() : LocalDateTime.now())
                .build();

        // 각 채팅방마다 다른 토픽(채널)으로 메시지 브로드캐스트
        // 예: "/topic/chatroom/{roomId}"
        String destination = "/topic/chatroom/" + messageDto.getRoomId();
        // SimpMessagingTemplate을 주입받아 해당 destination으로 메시지를 전송
        messagingTemplate.convertAndSend(destination, response);

    }
}
