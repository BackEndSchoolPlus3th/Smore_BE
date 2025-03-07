package com.meossamos.smore.domain.chat.message.controller;

import com.meossamos.smore.domain.chat.message.dto.ChatMessageRequestDto;
import com.meossamos.smore.domain.chat.message.dto.ChatMessageResponseDto;
import com.meossamos.smore.domain.chat.message.entity.ChatMessage;
import com.meossamos.smore.domain.chat.message.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class MessageController {

    private final ChatMessageService chatMessageService;

    // 채팅방의 기존 메시지 불러오기(dm, group구분은 chatType)
    @GetMapping("/{chatType}/{roomId}/messages")
    public ResponseEntity<List<ChatMessage>> getChatMessages(
            @PathVariable("chatType") String chatType,
            @PathVariable("roomId") String roomId) {
        List<ChatMessage> messages = chatMessageService.findMessagesList(roomId, chatType);
        return ResponseEntity.ok(messages);
    }

    // DM 메시지 전송
    @PostMapping("/dm/{roomId}/messages")
    public ResponseEntity<ChatMessageResponseDto> sendDmMessage(
            @PathVariable("roomId") String roomId,
            @RequestBody ChatMessageRequestDto messageRequestDto) {

        ChatMessage savedMessage = chatMessageService.saveChatMessage(
                roomId,
                "dm",
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

    // 그룹 채팅 메시지 전송
    @PostMapping("/group/{roomId}/messages")
    public ResponseEntity<ChatMessageResponseDto> sendGroupMessage(
            @PathVariable("roomId") String roomId,
            @RequestBody ChatMessageRequestDto messageRequestDto) {

        ChatMessage savedMessage = chatMessageService.saveChatMessage(
                roomId,
                "group",
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
