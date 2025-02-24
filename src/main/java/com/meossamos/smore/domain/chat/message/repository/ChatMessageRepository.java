package com.meossamos.smore.domain.chat.message.repository;

import com.meossamos.smore.domain.chat.message.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
}
