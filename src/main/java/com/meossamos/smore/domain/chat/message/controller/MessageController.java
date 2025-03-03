package com.meossamos.smore.domain.chat.message.controller;

import com.meossamos.smore.domain.chat.message.dto.ChatMessageResponseDto;
import com.meossamos.smore.domain.chat.message.entity.ChatMessage;
import com.meossamos.smore.domain.chat.message.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat/messages")
@RequiredArgsConstructor
public class MessageController {

    private final ChatMessageService chatMessageService;

    /**
     * 특정 채팅방의 메시지 이력을 조회합니다.
     * GET /api/chat/messages/{roomId}
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<List<ChatMessageResponseDto>> getMessages(@PathVariable String roomId) {
        List<ChatMessage> messages = chatMessageService.findMessagesList(roomId);

        // 엔티티를 Response DTO로 변환
        List<ChatMessageResponseDto> dtos = messages.stream().map(message ->
                ChatMessageResponseDto.builder()
                        .messageId(message.getId())
                        .roomId(message.getRoomId())
                        .senderId(message.getSenderId())
                        .message(message.getMessage())
                        .attachment(message.getAttachment())
                        .timestamp(message.getCreatedDate() != null ? message.getCreatedDate() : LocalDateTime.now())
                        .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
