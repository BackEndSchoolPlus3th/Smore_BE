package com.meossamos.smore.domain.chat.webMedia.controller;


import com.meossamos.smore.domain.chat.webMedia.model.VCMessage.MessageDto;
import com.meossamos.smore.domain.chat.webMedia.service.VCService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@ToString
@Controller
@RequiredArgsConstructor
@Slf4j
public class VCController {

    private final SimpMessagingTemplate messagingTemplate;
    private final VCService vcService;


    @MessageMapping("/vc/test")
    @SendTo("/topic/test")
    public String test(){
        System.out.println("test success");
        return "test success";
    }


    @MessageMapping("/vc/{roomId}")
    public void handleMessage(
            @DestinationVariable String roomId,
            @Payload MessageDto<?> messageDto,
            Principal principal
            ){

        VCService.OutMessages out = vcService.handleMessage(roomId, principal.getName(), messageDto);

        if(out.ack() !=null){
            messagingTemplate.convertAndSendToUser(principal.getName(),
                    "/queue/vc",
                    Map.of("ok", true, "roomId", roomId));
        }

        if(out.broadcasts() != null) {
            messagingTemplate.convertAndSend("/topic/vc/" + roomId, messageDto);
        }
        System.out.println("VCController got message -> roomId is "+roomId +" Message is "+ messageDto.toString());
    }

}
