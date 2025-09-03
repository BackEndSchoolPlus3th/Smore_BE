package com.meossamos.smore.domain.chat.webMedia.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class WebMediaController {

    @MessageMapping("/vc/test") // 클라: /app/chat/sendMessage 로 publish
    public void recv(@Payload String body,
                     StompHeaderAccessor accessor) {
        // 헤더 확인(Authorization 등)
        var headers = accessor.toNativeHeaderMap();
        log.info("[RECV] headers={}, body={}", headers, body);
        // 응답/브로드캐스트는 아직 하지 않음(수신만 확인)
    }
}
