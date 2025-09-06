package com.meossamos.smore.domain.chat.webMedia.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class VCController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/vc/test")
    @SendTo("/topic/test")
    public String test(){
        System.out.println("test success");
        return "test success";
    }
}
