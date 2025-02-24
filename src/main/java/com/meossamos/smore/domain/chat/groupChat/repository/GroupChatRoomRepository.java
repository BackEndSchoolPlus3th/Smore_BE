package com.meossamos.smore.domain.chat.groupChat.repository;

import com.meossamos.smore.domain.chat.groupChat.entity.GroupChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupChatRoomRepository extends JpaRepository<GroupChatRoom, Long> {
}
