package com.meossamos.smore.domain.chat.groupChat.service;

import com.meossamos.smore.domain.chat.groupChat.entity.GroupChatRoom;
import com.meossamos.smore.domain.chat.groupChat.repository.GroupChatRoomRepository;
import com.meossamos.smore.domain.study.study.entity.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupChatRoomService {
    private final GroupChatRoomRepository groupChatRoomRepository;

    public GroupChatRoom saveGroupChatRoom(Study study) {
        return groupChatRoomRepository.save(GroupChatRoom.builder().study(study).build());
    }
}
