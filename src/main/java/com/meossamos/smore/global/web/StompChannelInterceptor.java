package com.meossamos.smore.global.web;

import com.meossamos.smore.global.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class StompChannelInterceptor implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private static final Map<String, Authentication> sessionAuthMap = new ConcurrentHashMap<>();

    public StompChannelInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) return message;

        String sessionId = accessor.getSessionId();
        log.debug("🔍 STOMP 메시지 처리: command={}, sessionId={}", accessor.getCommand(), sessionId);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (tokenProvider.validateToken(token)) {
                    Authentication authentication = tokenProvider.getAuthentication(token);
                    accessor.setUser(authentication);
                    sessionAuthMap.put(sessionId, authentication);
                    log.debug("✅ STOMP CONNECT: 사용자 {} 인증 성공", authentication.getName());
                } else {
                    throw new AccessDeniedException("유효하지 않은 JWT 토큰");
                }
            } else {
                throw new AccessDeniedException("Authorization 헤더 없음");
            }
        } else if (StompCommand.SEND.equals(accessor.getCommand())) {
            if (accessor.getUser() == null && sessionId != null) {
                accessor.setUser(sessionAuthMap.get(sessionId));
                SecurityContextHolder.getContext().setAuthentication(sessionAuthMap.get(sessionId));
                log.debug("STOMP SEND: 세션에서 사용자 복원: {}", sessionAuthMap.get(sessionId).getName());
            }
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            sessionAuthMap.remove(sessionId);
            log.debug("STOMP DISCONNECT: 세션 제거 (sessionId={})", sessionId);
        }

        return message;
    }
}
