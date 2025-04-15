package com.meossamos.smore.domain.chat.videochat.controller;

import com.meossamos.smore.domain.chat.videochat.dto.SignalMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ApiV1SignallingController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/signal")
    public void handleSignal(SignalMessageDto signalMessageDto) {
        String destination = "/topic/signal/" + signalMessageDto.getRoomId();
        simpMessagingTemplate.convertAndSend(destination, signalMessageDto);
    }
}
