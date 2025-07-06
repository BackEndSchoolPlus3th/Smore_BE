package com.meossamos.smore.domain.chat.videochat.dto;

import lombok.Synchronized;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomSessionRegistry {
    private final Map<String, Set<String>> roomUserMap = new ConcurrentHashMap<>();

    public synchronized void addUserToRoom(String roomId, String userId) {
        roomUserMap.computeIfAbsent(roomId, k -> new HashSet<>()).add(userId);
    }

    @Synchronized
    public int getUserCount(String roomId) {
        return roomUserMap.getOrDefault(roomId, Collections.emptySet()).size();
    }
}
