package com.meossamos.smore.domain.chat.webMedia.service;

import com.meossamos.smore.domain.chat.webMedia.model.VCMessage.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class VCService {
    private final RoomAgent roomAgent;

    public void handleMassage(Long roomId, String name, MessageDto<?> messageDto) {

        switch (messageDto.getType()) {
            case joinRequestPayload: {
                boolean ok = roomAgent.handleJoin(messageDto);
                break;
            }

            case userLeftPayload: {
                roomAgent.handleLeave(messageDto);
                break;
            }

            case publishReportPayload: {
                roomAgent.handlePublish(messageDto);
                break;
            }
        }


        System.out.println("서버에서 메세지 받았습니다."
                +"roomId : "
                +roomId
                +" user : "
                +name
                +" MessageDto : "
                +messageDto);
    }
}
