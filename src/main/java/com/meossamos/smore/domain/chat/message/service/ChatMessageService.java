package com.meossamos.smore.domain.chat.message.service;

import com.meossamos.smore.domain.chat.message.entity.ChatMessage;
import com.meossamos.smore.domain.chat.message.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    // 채팅 메세지 저장
    public ChatMessage saveChatMessage(String roomId, String senderId, String message, String attachment) {

        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(roomId)
                .senderId(senderId)
                .message(message)
                .attachment(attachment)
                .build();

        return chatMessageRepository.save(chatMessage);
    }

    // 특정 채팅방의 메시지 목록 조회 (그룹, 1:1 모두)
    public List<ChatMessage> findMessagesList (String roomId) {
        return chatMessageRepository.findByRoomId(roomId);
    }

    // 특정 사용자가 보낸 메세지 조회
    public List<ChatMessage> findMessages (String senderId) {
        return chatMessageRepository.findBySenderId(senderId);
    }

    // 특정 채팅방의 모든 메시지 삭제 (MongoDB, 채팅방 삭제 시 필요)
    public void deleteMessagesByRoomId(String roomId) {
        chatMessageRepository.deleteByRoomId(roomId);
    }
}
