package com.meossamos.smore.domain.chat.chat.repository;

import com.meossamos.smore.domain.chat.chat.entity.ChatRoom;
import com.meossamos.smore.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// 1:1 채팅방 생성 및 조회
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 특정 두 유저 간의 1:1 채팅방 조회 (중복 방지)
    Optional<ChatRoom> findByMember1AndMember2(Member member1, Member member2);
    // 특정 사용자가 속한 모든 1:1 채팅방 조회
    List<ChatRoom> findByMember1OrMember2(Member member1, Member member2);
    // 특정 1:1 채팅방 삭제 (JPA에서 제공하는 deleteById 사용)
    void deleteById(Long chatRoomId);
}
