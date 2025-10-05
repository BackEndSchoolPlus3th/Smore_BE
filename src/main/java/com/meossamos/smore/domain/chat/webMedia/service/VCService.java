package com.meossamos.smore.domain.chat.webMedia.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meossamos.smore.domain.chat.webMedia.model.VCMessage.MessageDto;
import com.meossamos.smore.domain.chat.webMedia.model.VCMessage.payload.*;
import com.mongodb.lang.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;



@Slf4j
@Service
@RequiredArgsConstructor
public class VCService {

    private final ObjectMapper objectMapper;

    private final RoomAgent roomAgent;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageIdGenerator idGen;

    @Value("${webMedia.apiUrl}")
    private String apiUrl;

    @Value("${webMedia.StreamUrl}")
    private String streamUrl;

    // VCController에 전달할 메세지 종류 정의
    public static record OutMessages(
       @Nullable MessageDto<?> ack,
       @Nullable MessageDto<?> broadcasts
    ){
        public static OutMessages empty() { return new OutMessages(null,null);}
        public static OutMessages justAck(MessageDto<?> ack) {return new OutMessages(ack,null);}
        public static OutMessages justBroadcast(MessageDto<?> bc) {return new OutMessages(null, bc);}
        public static OutMessages both(MessageDto<?> ack, MessageDto<?> bc){return  new OutMessages(ack, bc);}
    }

    // 페이로드 꺼내는 헬퍼
    private <T> T payloadOf(MessageDto<?> dto, Class<T> clazz) {
        return objectMapper.convertValue(dto.getPayload(), clazz);
    }


    public OutMessages handleMessage(String roomId, String name, MessageDto<?> messageDto) {

        switch (messageDto.getType()) {
            case joinRequestPayload: {
                boolean ok = roomAgent.handleJoin(messageDto);
                System.out.println("VCService Log => handle join의 결과는 " + ok);

                if (ok) {
                    var ackPayload = JoinResponsePayload.builder()
                            .apiUrl(apiUrl)
                            .streamUrl(streamUrl)
                            .userId(name)
                            .build();

                    var ack = MessageDto.builder()
                            .messageId(idGen.next())
                            .type(MessageType.joinResponsePayload)
                            .userId(name)
                            .payload(ackPayload)
                            .roomId(roomId)
                            .sentAt(LocalDateTime.now())
                            .build();



                    var joinEvent = MessageDto.builder()
                            .messageId(idGen.next())
                            .roomId(roomId)
                            .userId(name)
                            .type(MessageType.joinResponsePayload)
                            .payload(null)
                            .sentAt(LocalDateTime.now())
                            .build();

//                    log.info("[SEND] roomId = {} msgId = {} type = {} ", roomId, ack.getMessageId(), ack.getType());
                    return OutMessages.both(ack, joinEvent);
                }
                return OutMessages.empty();
            }

            case leaveRequestPayload: {
                boolean ok = roomAgent.handleLeave(messageDto);
                if (ok) {
                    var eventPayload = UserLeftPayload.builder()
                            .build();

                    var leftEvent = MessageDto.builder()
                            .messageId(idGen.next())
                            .roomId(roomId)
                            .userId(name)
                            .type(MessageType.userLeftPayload)
                            .payload(eventPayload)
                            .sentAt(LocalDateTime.now())
                            .build();

                    log.info("[SEND] roomId = {} msgId = {} type = {} ", roomId, leftEvent.getMessageId(), leftEvent.getType());
                    return OutMessages.justBroadcast(leftEvent);
                }
                return OutMessages.empty();
            }

            case publishReportPayload: {
                PublishReportPayload pr = payloadOf(messageDto, PublishReportPayload.class);
                boolean desired = pr.isPublished();

                boolean ok = roomAgent.handlePublish(messageDto);
                if (ok) {
                    var eventPayload = PublishEventPayload.builder()
                            .published(desired)
                            .build();

                    var publishEvent = MessageDto.<PublishEventPayload>builder()
                            .messageId(idGen.next())
                            .roomId(roomId)
                            .userId(name)
                            .type(MessageType.publishEventPayload)
                            .payload(eventPayload)
                            .sentAt(LocalDateTime.now())
                            .build();

                    return OutMessages.justBroadcast(publishEvent);
                }
                return OutMessages.empty();
            }


            default: {
                return OutMessages.empty();
            }
        }
    }
}
