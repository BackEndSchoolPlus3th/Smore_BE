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

        // IN 로그
        log.info("[IN ] roomId ={} user ={} type ={} msgId ={}",
                roomId, principal.getName(), messageDto.getType(), messageDto.getMessageId());

        VCService.OutMessages out = vcService.handleMessage(roomId, principal.getName(), messageDto);

        // 개인 Ack 전송
        if(out.ack() !=null){
            messagingTemplate.convertAndSendToUser(principal.getName(),
                    "/queue/vc",out.ack());
            log.info("[OUT] ACK dest=/user/{}/queue/vc type ={} msgId={}",
                    principal.getName(), out.ack().getType(), out.ack().getMessageId());

        }

        // 브로드캐스트 전송
        if(out.broadcasts() != null) {
            messagingTemplate.convertAndSend("/topic/vc/" + roomId, out.broadcasts());
            log.info("[OUT] BCAST dest =/topic/vc/{} type={} msgId={}",
                    roomId, out.broadcasts().getType(), out.broadcasts().getMessageId());

        }
    }

}
