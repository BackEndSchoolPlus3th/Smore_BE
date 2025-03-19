package com.meossamos.smore.domain.chat.groupChat.repository;

import com.meossamos.smore.domain.chat.groupChat.entity.GroupChatRoom;
import com.meossamos.smore.domain.study.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GroupChatRoomRepository extends JpaRepository<GroupChatRoom, Long> {
    // 특정 스터디에 해당하는 그룹 채팅방 조회
    Optional<GroupChatRoom> findByStudy(Study study);
    //


}
