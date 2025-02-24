package com.meossamos.smore.domain.chat.chat.service;

import com.meossamos.smore.domain.chat.chat.entity.ChatRoom;
import com.meossamos.smore.domain.chat.chat.repository.ChatRoomRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom saveChatRoom(Member member1, Member member2) {
        ChatRoom chatRoom = ChatRoom.builder()
                .member1(member1)
                .member2(member2)
                .build();

        return chatRoomRepository.save(chatRoom);
    }
}
