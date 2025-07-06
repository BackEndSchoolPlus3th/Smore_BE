package com.meossamos.smore.domain.chat.videochat.handler;

import com.meossamos.smore.domain.chat.videochat.registry.RoomSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketDisconnectHandler {

    private final RoomSessionRegistry roomSessionRegistry;

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
        roomSessionRegistry.removeUserBySessionId(sessionId);
        log.info("🔌 연결 종료됨 - 세션 ID 제거됨: {}", sessionId);
    }
}
