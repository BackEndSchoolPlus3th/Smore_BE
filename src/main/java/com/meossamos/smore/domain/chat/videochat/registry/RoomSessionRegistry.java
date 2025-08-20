package com.meossamos.smore.domain.chat.videochat.registry;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomSessionRegistry {
    // roomId -> 세션 ID 목록
    private final Map<String, Set<String>> roomUserMap = new ConcurrentHashMap<>();

    // sessionId -> roomId (역방향)
    private final Map<String, String> sessionToRoomMap = new ConcurrentHashMap<>();

    // 참가자 추가
    public synchronized void addUserToRoom(String roomId, String sessionId) {
        roomUserMap.computeIfAbsent(roomId, k -> new HashSet<>()).add(sessionId);
        sessionToRoomMap.put(sessionId, roomId);
    }

    // 참가자 수 조회
    public synchronized int getUserCount(String roomId) {
        return roomUserMap.getOrDefault(roomId, Collections.emptySet()).size();
    }

    // 세션 제거
    public synchronized void removeUserBySessionId(String sessionId) {
        String roomId = sessionToRoomMap.get(sessionId);
        if (roomId != null) {
            Set<String> users = roomUserMap.get(roomId);
            if (users != null) {
                users.remove(sessionId);
                if (users.isEmpty()) {
                    roomUserMap.remove(roomId); // 방이 비면 제거
                }
            }
            sessionToRoomMap.remove(sessionId);
        }
    }
}
