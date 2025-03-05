package com.meossamos.smore.domain.chat.message.controller;

import com.meossamos.smore.domain.chat.message.dto.ChatMessageRequestDto;
import com.meossamos.smore.domain.chat.message.dto.ChatMessageResponseDto;
import com.meossamos.smore.domain.chat.message.entity.ChatMessage;
import com.meossamos.smore.domain.chat.message.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class MessageController {

    private final ChatMessageService chatMessageService;

    /**
     * DM 메시지 전송
     * POST /api/chatrooms/dm/{roomId}/messages
     */
    @PostMapping("/dm/{roomId}/messages")
    public ResponseEntity<ChatMessageResponseDto> sendDmMessage(
            @PathVariable("roomId") String roomId,
            @RequestBody ChatMessageRequestDto messageRequestDto) {

        ChatMessage savedMessage = chatMessageService.saveChatMessage(
                roomId,
                messageRequestDto.getSenderId(),
                messageRequestDto.getMessage(),
                messageRequestDto.getAttachment()
        );

        ChatMessageResponseDto response = ChatMessageResponseDto.builder()
                .messageId(savedMessage.getId())
                .roomId(savedMessage.getRoomId())
                .senderId(savedMessage.getSenderId())
                .message(savedMessage.getMessage())
                .attachment(savedMessage.getAttachment())
                .timestamp(savedMessage.getCreatedDate() != null ? savedMessage.getCreatedDate() : LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * 그룹 채팅 메시지 전송
     * POST /api/chatrooms/group/{roomId}/messages
     */
    @PostMapping("/group/{roomId}/messages")
    public ResponseEntity<ChatMessageResponseDto> sendGroupMessage(
            @PathVariable("roomId") String roomId,
            @RequestBody ChatMessageRequestDto messageRequestDto) {

        ChatMessage savedMessage = chatMessageService.saveChatMessage(
                roomId,
                messageRequestDto.getSenderId(),
                messageRequestDto.getMessage(),
                messageRequestDto.getAttachment()
        );

        ChatMessageResponseDto response = ChatMessageResponseDto.builder()
                .messageId(savedMessage.getId())
                .roomId(savedMessage.getRoomId())
                .senderId(savedMessage.getSenderId())
                .message(savedMessage.getMessage())
                .attachment(savedMessage.getAttachment())
                .timestamp(savedMessage.getCreatedDate() != null ? savedMessage.getCreatedDate() : LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
