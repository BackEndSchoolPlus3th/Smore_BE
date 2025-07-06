package com.meossamos.smore.domain.chat.videochat.controller;

import com.meossamos.smore.domain.chat.videochat.registry.RoomSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.Header;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ApiV1SignallingController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RoomSessionRegistry roomSessionRegistry;

    @MessageMapping("/signal/send/{roomId}")
    public void signal(
            @DestinationVariable String roomId,
            @Header("simpSessionId") String sessionId,
            @Payload Map<String, Object> message
            ) {
            System.out.println("📨 Signal Received from {}: {}: "+sessionId + message);

            // 참가자 등록
            roomSessionRegistry.addUserToRoom(roomId, sessionId);

            int userCount = roomSessionRegistry.getUserCount(roomId);
            log.info("현재 방 {} 참가자 수: {}", roomId, userCount);

            message.put("userCount", userCount);
            simpMessagingTemplate.convertAndSend("/topic/signal/" + roomId, message);
    }

/* 테스트 */
//    @MessageMapping("/signal/send/{roomId}")
//    public void handleSignalMessage(@DestinationVariable String roomId,
//                                    @Payload SignalMessageDto message) {
//        log.info("📥 받은 메시지 (roomId: {}): {}", roomId, message);
//
//        // 테스트용으로 pong 응답
//        if ("ping".equals(message.getType())) {
//            SignalMessageDto pong = new SignalMessageDto("pong", "서버 응답: pong!");
//            simpMessagingTemplate.convertAndSend("/topic/signal/" + roomId, pong);
//            log.info("📤 pong 전송 완료");
//        }
//    }


}

