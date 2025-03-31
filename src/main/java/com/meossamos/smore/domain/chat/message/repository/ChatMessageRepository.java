package com.meossamos.smore.domain.chat.message.repository;

import com.meossamos.smore.domain.chat.message.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// MongoDB에서 메시지 저장 및 조회
@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    // 특정 채팅방의 메시지 목록 조회 (dm, group)
//    List<ChatMessage> findByRoomId(String roomId);
    List<ChatMessage> findByRoomIdAndChatType(String roomId, String chatType);
    // 특정 사용자가 보낸 메시지 조회
    List<ChatMessage> findBySenderId(String senderId);
    // 특정 채팅방의 모든 메시지 삭제 (MongoDB, 채팅방 삭제 시 필요)
    void deleteByRoomId(String roomId);
}
