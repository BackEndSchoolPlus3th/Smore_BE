package com.meossamos.smore.global.web;

import com.meossamos.smore.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.security.core.Authentication;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class TokenHandshakeInterceptor implements HandshakeInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String token = servletRequest.getServletRequest().getParameter("token");

            if (token != null && tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                attributes.put("token", token);
                attributes.put("authentication", authentication); // 인증 정보 저장
                log.debug("✅ WebSocket Handshake 성공: 사용자 {} 인증됨", authentication.getName());
            } else {
                log.warn("⚠️ WebSocket Handshake 실패: 유효하지 않은 토큰");
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // 후처리 필요 없음
    }
}
