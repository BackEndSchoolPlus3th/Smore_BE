package com.meossamos.smore.domain.chat.message.service;

import com.meossamos.smore.domain.chat.message.entity.ChatMessage;
import com.meossamos.smore.domain.chat.message.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage saveChatMessage(String roomId, String senderId, String message, String attachment) {

        ChatMessage chatMessage = ChatMessage.builder()
                .roomId(roomId)
                .senderId(senderId)
                .message(message)
                .attachment(attachment)
                .build();

        return chatMessageRepository.save(chatMessage);
    }
}
