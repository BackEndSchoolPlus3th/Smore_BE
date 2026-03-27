package com.meossamos.smore.domain.chat.webMedia.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meossamos.smore.domain.chat.webMedia.model.RoomUser;
import com.meossamos.smore.domain.chat.webMedia.model.VCMessage.MessageDto;
import com.meossamos.smore.domain.chat.webMedia.model.VCMessage.payload.*;
import com.mongodb.lang.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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

    // VCController에 전달할 메세지 종류 정의!
    public static record OutMessages(
       @Nullable MessageDto<?> ack,
       List<MessageDto<?>> broadcasts
    ){
        public static OutMessages empty() { return new OutMessages(null,List.of());}
        public static OutMessages justAck(MessageDto<?> ack) {return new OutMessages(ack,List.of());}
        public static OutMessages justBroadcast(MessageDto<?> bc) {return new OutMessages(null, List.of(bc));}
        public static OutMessages both(@Nullable MessageDto<?> ack, List<MessageDto<?>> bcs){return  new OutMessages(ack, (bcs==null?List.of():bcs));}
    }

    // 페이로드 꺼내는 헬퍼
    private <T> T payloadOf(MessageDto<?> dto, Class<T> clazz) {
        return objectMapper.convertValue(dto.getPayload(), clazz);
    }


    private MessageDto<Void> buildJoinEvent(String roomId, String userId){
        return MessageDto.<Void>builder()
                .messageId(idGen.next())
                .roomId(roomId)
                .userId(userId)
                .type(MessageType.joinEventPayload)
                .payload(null)
                .sentAt(LocalDateTime.now())
                .build();
    }

    private MessageDto<PublishEventPayload> buildPublicEvent(String roomId, String userId, boolean published) {

        var payload = PublishEventPayload.builder()
                .published(published)
                .build();

        return MessageDto.<PublishEventPayload>builder()
                .messageId(idGen.next())
                .roomId(roomId)
                .userId(userId)
                .type(MessageType.publishEventPayload)
                .payload(payload)
                .sentAt(LocalDateTime.now())
                .build();
    }




    public OutMessages handleMessage(String roomId, String name, MessageDto<?> messageDto) {
        String clientMessageId = messageDto.getMessageId();
        switch (messageDto.getType()) {
            case joinRequestPayload: {
                RoomAgent.JoinResult result = roomAgent.handleJoin(messageDto);
                log.info("VCService Log => handle join의 결과는 " + result);

                RoomUser myUser = result.user();
                String verifiedUserId = myUser.getUserId();
                RoomUser anotherUser = result.anotherUser();

                var ackPayload = JoinResponsePayload.builder()
                        .apiUrl(apiUrl)
                        .streamUrl(streamUrl)
                        .roomId(roomId)
                        .user(myUser)
                        .anotherUser(anotherUser)
                        .build();

                var ack = MessageDto.<JoinResponsePayload>builder()
                        .messageId(clientMessageId)
                        .type(MessageType.joinResponsePayload)
                        .userId(verifiedUserId)
                        .payload(ackPayload)
                        .roomId(roomId)
                        .sentAt(LocalDateTime.now())
                        .build();

                var joinEvent = MessageDto.<Void>builder()
                        .messageId(clientMessageId + "_ev")
                        .roomId(roomId)
                        .userId(verifiedUserId)
                        .type(MessageType.joinEventPayload)
                        .sentAt(LocalDateTime.now())
                        .build();
                log.info("VCService => 생성된 Ack ID: {}", ack.getMessageId());
                log.info("VCService => 생성된 Event ID: {}", joinEvent.getMessageId());
                return OutMessages.both(ack, List.of(joinEvent));

            }

            case leaveRequestPayload: {
                boolean ok = roomAgent.handleLeave(messageDto);
                if (ok) {


                    var leftEvent = MessageDto.builder()
                            .messageId(clientMessageId)
                            .roomId(roomId)
                            .userId(name)
                            .type(MessageType.userLeftPayload)
                            .payload(null)
                            .sentAt(LocalDateTime.now())
                            .build();

//                    log.info("[SEND] roomId = {} msgId = {} type = {} ", roomId, leftEvent.getMessageId(), leftEvent.getType());
                    return OutMessages.justBroadcast(leftEvent);
                }
                return OutMessages.empty();
            }

            case publishReportPayload: {
                PublishReportPayload pr = payloadOf(messageDto, PublishReportPayload.class);
                boolean desired = pr.isPublished();

                List<MessageDto<?>> bcasts = new ArrayList<>();

                boolean joinedNow = roomAgent.ensureMember(roomId, name, LocalDateTime.now());
                if (joinedNow) {
                    bcasts.add(buildJoinEvent(roomId, name));
                }

                boolean changed = roomAgent.setPublished(roomId, name, desired);
                    if (changed) {
                        bcasts.add(buildPublicEvent(roomId, name, desired));
                    }
                    return bcasts.isEmpty() ? OutMessages.empty()
                            : OutMessages.both(null, bcasts);
                }

            default: {
                return OutMessages.empty();
            }
        }
    }
}
