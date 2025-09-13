package com.meossamos.smore.domain.chat.webMedia.service;

import com.meossamos.smore.domain.chat.webMedia.model.RoomUser;
import com.meossamos.smore.domain.chat.webMedia.model.VCMessage.MessageDto;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@ToString
public class RoomAgent {

    private final Map<String, Map<String, RoomUser>> rooms = new ConcurrentHashMap<>();


    public boolean handleJoin(MessageDto<?>  messageDto) {
        String roomId = messageDto.getRoomId();
        String userId = messageDto.getUserId();

        var room = rooms.computeIfAbsent(roomId, id -> new ConcurrentHashMap<>());


        LocalDateTime joinedAt = messageDto.getSentAt() != null ? messageDto.getSentAt() : LocalDateTime.now();


        final RoomUser roomUser = RoomUser.builder()
                .roomId(roomId)
                .userId(userId)
                .published(false)
                .joinedAt(joinedAt)
                .build();

        // 없을 때만 추가 (원자적)
        boolean added = room.putIfAbsent(userId, roomUser) == null;

        System.out.println("서버에 join요청 메세지 전달됨" +"roomId = "+roomId+" map = " +rooms.toString());

        return added;

    }

    public void handleLeave(MessageDto<?> messageDto) {
    }

    public void handlePublish(MessageDto<?> messageDto) {

    }
}
