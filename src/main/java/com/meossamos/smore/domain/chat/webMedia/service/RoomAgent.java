package com.meossamos.smore.domain.chat.webMedia.service;

import com.meossamos.smore.domain.chat.webMedia.model.RoomUser;
import com.meossamos.smore.domain.chat.webMedia.model.VCMessage.MessageDto;
import com.meossamos.smore.domain.chat.webMedia.model.VCMessage.payload.PublishReportPayload;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
@ToString
public class RoomAgent {

    private final Map<String, RoomState> rooms = new ConcurrentHashMap<>();

    static final class  RoomState{
        final ConcurrentHashMap<String, RoomUser> users = new ConcurrentHashMap<>();
        volatile LocalDateTime createdAt = LocalDateTime.now();
        volatile LocalDateTime lastActivatedAt = LocalDateTime.now();
        volatile LocalDateTime emptyAt = null;
        final AtomicInteger publishedCount = new AtomicInteger(0);
        final AtomicLong version = new AtomicLong(0);
    }

    public boolean handleJoin(MessageDto<?>  messageDto) {
        String roomId = messageDto.getRoomId();
        String userId = messageDto.getUserId();
        LocalDateTime joinedAt = messageDto.getSentAt() != null ? messageDto.getSentAt() : LocalDateTime.now();

        final RoomUser roomUser = RoomUser.builder()
                .roomId(roomId)
                .userId(userId)
                .published(false)
                .joinedAt(joinedAt)
                .build();

        RoomState state = rooms.computeIfAbsent(roomId, id -> new RoomState());
        boolean added = state.users.putIfAbsent(userId, roomUser) == null;

        if(added){
            state.emptyAt = null;
            state.lastActivatedAt = LocalDateTime.now();
            state.version.incrementAndGet();
        }

        return added;

    }

    public boolean handleLeave(MessageDto<?> messageDto) {
        String roomId = messageDto.getRoomId();
        String userId = messageDto.getUserId();

        final boolean[] removed = {false};

        rooms.computeIfPresent(roomId, (id, state) -> {
            RoomUser old = state.users.remove(userId);
            removed[0] = (old != null);
            if (!removed[0]) return  state;

            if (old.isPublished()) state.publishedCount.decrementAndGet();
            state.lastActivatedAt = LocalDateTime.now();
            state.version.incrementAndGet();

            // 즉시 지우지 않고 기록만 우선
            if (state.users.isEmpty()){
                state.emptyAt = LocalDateTime.now();
            }
            return state;
        });
        return removed[0];
    }

    public boolean handlePublish(MessageDto<?> messageDto) {
        String roomId = messageDto.getRoomId();
        String userId = messageDto.getUserId();

        boolean desired = ((PublishReportPayload) messageDto.getPayload()).isPublished();

        final boolean[] changed = {false};

        rooms.computeIfPresent(roomId, (id, state) -> {
            state.users.computeIfPresent(userId, (uid, old) -> {
                if (old.isPublished() == desired) return old; // 변화없음
                RoomUser updated = old.toBuilder().published(desired).build();
                if (desired) state.publishedCount.incrementAndGet();
                else state.publishedCount.decrementAndGet();
                state.lastActivatedAt = LocalDateTime.now();
                state.version.incrementAndGet();
                changed[0] = true;
                return updated;
            });
            return state;
        });
        return changed[0];
    }
}

