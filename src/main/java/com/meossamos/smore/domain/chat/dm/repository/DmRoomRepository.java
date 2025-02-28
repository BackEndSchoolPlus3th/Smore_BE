package com.meossamos.smore.domain.chat.dm.repository;

import com.meossamos.smore.domain.chat.dm.entity.DmRoom;
import com.meossamos.smore.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// 1:1 채팅방 생성 및 조회
@Repository
public interface DmRoomRepository extends JpaRepository<DmRoom, Long> {
    // 특정 두 유저 간의 1:1 채팅방 조회 (중복 방지)
    @Query("SELECT d FROM DmRoom d WHERE (d.member1 = :member1 AND d.member2 = :member2) " +
            "OR (d.member1 = :member2 AND d.member2 = :member1)")
    Optional<DmRoom> findByMembers(@Param("member1") Member member1, @Param("member2") Member member2);
    // 특정 사용자가 속한 모든 1:1 채팅방 조회
    List<DmRoom> findByMember1OrMember2(Member member1, Member member2);
    // 특정 1:1 채팅방 삭제 (JPA에서 제공하는 deleteById 사용)
    void deleteById(Long dmRoomId);
}
