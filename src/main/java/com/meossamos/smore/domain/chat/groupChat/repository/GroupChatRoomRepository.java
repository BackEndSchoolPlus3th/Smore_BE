package com.meossamos.smore.domain.chat.groupChat.repository;

import com.meossamos.smore.domain.chat.groupChat.entity.GroupChatRoom;
import com.meossamos.smore.domain.study.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// 스터디 그룹 채팅방 관리
@Repository
public interface GroupChatRoomRepository extends JpaRepository<GroupChatRoom, Long> {
    // 특정 스터디 그룹의 채팅방 조회
    Optional<GroupChatRoom> findByStudy(Study study);
    // 사용자가 참여 중인 그룹 채팅방 목록 조회 (스터디 ID 리스트 활용)
    List<GroupChatRoom> findByStudyIdIn(List<String> studyIds);
}
