package com.meossamos.smore.domain.chat.groupChat.service;

import com.meossamos.smore.domain.chat.groupChat.entity.GroupChatRoom;
import com.meossamos.smore.domain.chat.groupChat.repository.GroupChatRoomRepository;
import com.meossamos.smore.domain.study.study.entity.Study;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupChatRoomService {
    private final GroupChatRoomRepository groupChatRoomRepository;

    @Transactional
    public GroupChatRoom createOrGetGroupChatRoom(Study study) {
        return groupChatRoomRepository.findByStudy(study)
                .orElseGet(() -> {
                    log.info("Creating new group chat room for study id: {}", study.getId());
                    GroupChatRoom room = GroupChatRoom.builder().study(study).build();
                    return groupChatRoomRepository.save(room);
                });
    }
}
