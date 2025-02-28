package com.meossamos.smore.domain.chat.chat.controller;

import com.meossamos.smore.domain.chat.chat.dto.ChatRoomRequestDto;
import com.meossamos.smore.domain.chat.chat.entity.ChatRoom;
import com.meossamos.smore.domain.chat.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatrooms")
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;
    // 채팅방 생성 api
    // API 요청 시 POST http://localhost:8090/api/chatrooms?member1Id=1&member2Id=2 호출 가능
    @PostMapping
    public ChatRoom createChatRoom(@RequestBody ChatRoomRequestDto requestDto) {
        return chatRoomService.createChatRoom(requestDto.getMember1Id(), requestDto.getMember2Id());
    }

    // 특정 1:1 채팅방 삭제
    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<String> deleteChatRoom(@PathVariable("chatRoomId") Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseEntity.ok("Deleted chat room with id " + chatRoomId);
    }

}
