package com.meossamos.smore.domain.chat.chat.service;

import com.meossamos.smore.domain.chat.chat.entity.ChatRoom;
import com.meossamos.smore.domain.chat.chat.repository.ChatRoomRepository;
import com.meossamos.smore.domain.chat.message.repository.ChatMessageRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// 1:1 채팅방 생성, 조회(단일), 목록 조회, 채팅방 존재 여부 확인
@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository; // 생성자 주입
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatRoom saveChatRoom(Member member1, Member member2) {
        return chatRoomRepository.save(ChatRoom.builder()
                .member1(member1)
                .member2(member2)
                .build());
    }
    // 채팅방 생성
    @Transactional
    public ChatRoom createChatRoom(Long member1Id, Long member2Id) {
        Member member1 = memberRepository.findById(member1Id)
                .orElseThrow(() -> new IllegalArgumentException("Member1 not found"));
        Member member2 = memberRepository.findById(member2Id)
                .orElseThrow(() -> new IllegalArgumentException("Member2 not found"));

        // 기존 1:1 채팅방이 있는지 확인 (중복 생성 방지)
        return chatRoomRepository.findByMember1AndMember2(member1, member2)
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder()
                        .member1(member1)
                        .member2(member2)
                        .build()));
    }

    // 특정 채팅방 조회
    public ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
    }

    // 사용자가 속한 모든 채팅방 조회
    public List<ChatRoom> getUserChatRooms(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return chatRoomRepository.findByMember1OrMember2(member, member);
    }

    // 1:1 채팅방 삭제 (연관 메시지도 함께 삭제)
    @Transactional
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        // 채팅방에 속한 모든 메세지 삭제 (MongoDB)
        chatMessageRepository.deleteByRoomId(chatRoomId.toString());
        // 채팅방 삭제 (JPA)
        chatRoomRepository.delete(chatRoom);
    }

    // 특정 두 사용자가 이미 1:1 채팅방이 있는지 확인
    public boolean isChatRoomExists(Long member1Id, Long member2Id) {
        Member member1 = memberRepository.findById(member1Id)
                .orElseThrow(() -> new IllegalArgumentException("Member1 not found"));
        Member member2 = memberRepository.findById(member2Id)
                .orElseThrow(() -> new IllegalArgumentException("Member2 not found"));

        return chatRoomRepository.findByMember1AndMember2(member1, member2).isPresent();
    }
}
