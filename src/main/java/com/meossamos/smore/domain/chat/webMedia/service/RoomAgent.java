package com.meossamos.smore.domain.chat.webMedia.service;

import com.meossamos.smore.domain.chat.webMedia.model.RoomUser;
import com.meossamos.smore.domain.chat.webMedia.model.VCMessage.MessageDto;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
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

        @Override public String toString(){
            return "RoomState{users= " + users.keySet()
                    + ", publishedCount= " + publishedCount.get()
                    + ", version= " + version.get()
                    + ", lastActivitiyAt= " + lastActivatedAt + "}";
        }

    }

    public boolean ensureMember(String roomId, String userId, LocalDateTime joinedAt){
        RoomState state = rooms.computeIfAbsent(roomId, id -> new RoomState());
        LocalDateTime ts = (joinedAt != null) ? joinedAt : LocalDateTime.now();

        RoomUser newUser = RoomUser.builder()
                .roomId(roomId)
                .userId(userId)
                .published(false)
                .joinedAt(ts)
                .build();

        boolean added = (state.users.putIfAbsent(userId,newUser)==null);
        if (added) {
            state.version.incrementAndGet();
            state.lastActivatedAt = ts;
            log.info("RoomAgent.ensureMember: room={} user={} ADDED (ver={})", roomId, userId, state.version.get());
        } else {
            log.debug("RoomAgent.ensureMember: room={} user={} already present", roomId, userId);
        }
        return added;
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
            log.info("RoomAgent Log => "+"RoomUser : " + roomUser.toString() + "Rooms : "+rooms.toString());
        }
        return added;
    }

    public boolean setPublished(String roomId, String userId, boolean desired){
        RoomState state = rooms.get(roomId);
        if(state==null){
            log.debug("RoomAgent.setPublished: room={} not found", roomId);
            return false;
        }

        final boolean[] changed = {false};
        state.users.computeIfPresent(userId, (uid,old)->{
            if (old.isPublished() == desired){
                return old; // 변화 없음
            }
            RoomUser updated = old.toBuilder().published(desired).build();

            if(desired) state.publishedCount.incrementAndGet();
            else        state.publishedCount.decrementAndGet();

            state.lastActivatedAt = LocalDateTime.now();
            long ver = state.version.incrementAndGet();
            changed[0] = true;

            log.info("RoomAgent.setPublished: room={} user={} {}->{} (ver={}, pubCnt={})",
                    roomId, userId, old.isPublished(), desired, ver, state.publishedCount.get());
            return updated;

        });
        return changed[0];
    }


    public boolean isMember(String roomId, String userId){
        RoomState state = rooms.get(roomId);
        return state != null && state.users.containsKey(userId);
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

            log.info("RoomAgent Log => "+ "Rooms : "+rooms.toString());
            return state;
        });
        log.info("RoomAgent Log => "+ "Rooms : "+rooms.toString());
        return removed[0];
    }

    public boolean handlePublish(String roomId, String userId, boolean desired) {
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
                log.info("RoomAgent Log => rooms : "+rooms.toString());
                return updated;
            });
            return state;
        });
        return changed[0];
    }
}

