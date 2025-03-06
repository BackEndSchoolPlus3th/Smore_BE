package com.meossamos.smore.domain.chat.dm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// STOMP 프로토콜과 SockJS를 사용하여 WebSocket 엔드포인트 등록
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*") // 실제 운영환경에서는 도메인 제한 필요
                .withSockJS();
    }

    // 메세지 브로커 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 메시지 전송 시 사용하는 prefix
        registry.setApplicationDestinationPrefixes("/app");
        // 클라이언트가 구독하는 메시지의 prefix
        registry.enableSimpleBroker("/topic");
    }
}
