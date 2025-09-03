package com.meossamos.smore.domain.chat.webMedia.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.meossamos.smore.domain.chat.webMedia.model.ErrorCode;
import com.meossamos.smore.domain.chat.webMedia.model.MessageType;
import com.meossamos.smore.domain.chat.webMedia.model.RoomUser;
import com.meossamos.smore.domain.chat.webMedia.model.message.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RoomAgent {
    private static final int MaxUsers = 10;
    private static final String ApiUrl = "http://localhost:1985";
    private static final String StreamUrl = "webrtc://localhost";

    private final ObjectMapper objectMapper;
    private final MessageSender messageSender;
    private final String roomId;

    private final Object lockObj;
    private final Map<String, RoomUser> roomUserMap;
    private int userIdCounter;

    public RoomAgent(ObjectMapper objectMapper, MessageSender messageSender, String roomId){
        this.objectMapper = objectMapper;
        this.messageSender = messageSender;
        this.roomId = roomId;

        this.lockObj = new Object();
        this.roomUserMap = new HashMap<>();
        this.userIdCounter = 0;
    }

    public String getRoomId(){
        return roomId;
    }

    public int getUserCount(){
        synchronized (lockObj){
            return roomUserMap.size();
        }
    }

    public void handleUserLeft(WebSocketSession session) throws Exception {
        synchronized (lockObj){
            final RoomUser user = roomUserMap.get(session.getId());

            if(user != null) {
                roomUserMap.remove(session.getId());

                final RoomUser anotherUser = this.getAnotherUser(user);
                if(anotherUser != null) {
                    final UserLeftEventMessage eventMessage = UserLeftEventMessage.builder()
                            .userId(user.getUserId())
                            .build();
                    messageSender.sendEventMessage(anotherUser.getSession(),roomId, MessageType.UserLeftEvent, eventMessage);
                }
            }
        }
    }

    public void handleMessage(WebSocketSession session,
                              String messageId, MessageType type, String messageStr) throws Exception {
        synchronized (lockObj) {
            if(MessageType.JoinRequest.equals(type)){
                final JoinRequestMessage message = objectMapper.readValue(messageStr, JoinRequestMessage.class);
                handleUserJoined(session, messageId, message);
            } else if (MessageType.UserPublishedChangeReport.equals(type)){
                final UserPublishedChangeReportMessage message = objectMapper.readValue(messageStr, UserPublishedChangeReportMessage.class);
                handleUserPublishedChangeReport(session, messageId, message);
            } else {
                final ErrorResponseMessage response = ErrorResponseMessage.builder()
                        .errorCode(ErrorCode.BadRequest)
                        .message("잘못된 요청이라고요")
                        .build();

                messageSender.sendTransactionMessage(session, roomId, "guest",messageId, MessageType.ErrorResponse, response);

            }
        }
    }

    private void handleUserJoined(WebSocketSession session, String messageId, JoinRequestMessage message) throws  Exception {
        if(roomUserMap.size() >= MaxUsers){
            final ErrorResponseMessage response = ErrorResponseMessage.builder()
                    .errorCode(ErrorCode.TooManyUsers)
                    .message("방의 최대 인원에 도달했습니다.")
                    .build();
            messageSender.sendTransactionMessage(session, roomId, "guest",
                    messageId,MessageType.ErrorResponse,response);
        }

        if(!roomId.equals(message.getRoomId())){
            final ErrorResponseMessage response = ErrorResponseMessage.builder()
                    .errorCode(ErrorCode.BadRequest)
                    .message("잘못된 요청입니다.")
                    .build();

            messageSender.sendTransactionMessage(session, roomId, "guest", messageId, MessageType.ErrorResponse, response);
        }

        // user 객체 생성
        final String userId = "user" + this.userIdCounter++;
        final RoomUser user = RoomUser.builder()
                .roomId(roomId)
                .userId(userId)
                .published(false)
                .session(session)
                .build();

        // join했다 알리기
        final RoomUser anotherUser = this.getAnotherUser(user);
        final JoinResponseMessage response = JoinResponseMessage.builder()
                .apiUrl(ApiUrl)
                .streamUrl(StreamUrl)
                .roomId(roomId)
                .user(user)
                .anotherUser(anotherUser)
                .build();
        roomUserMap.put(session.getId(), user);

        messageSender.sendTransactionMessage(session, roomId, userId, messageId, MessageType.JoinResponse, response);

        if(anotherUser != null) {
            final UserJoinedEventMessage eventMessage = UserJoinedEventMessage.builder()
                    .user(user)
                    .build();
            messageSender.sendEventMessage(anotherUser.getSession(), roomId, MessageType.UserJoinedEvent, eventMessage);
        }
    }

    private void handleUserPublishedChangeReport(WebSocketSession session, String messageId, UserPublishedChangeReportMessage message) throws Exception {
        final RoomUser user = roomUserMap.get(session.getId());

        if((user != null) && (user.isPublished() != message.isPublished())){
            user.setPublished(message.isPublished());

            final RoomUser anotherUser = getAnotherUser(user);

            if(anotherUser != null) {
                final UserStateChangedEventMessage eventMessage = UserStateChangedEventMessage.builder()
                        .userId(user.getUserId())
                        .published(message.isPublished())
                        .build();

                messageSender.sendEventMessage(anotherUser.getSession(), roomId, MessageType.UserStateChangedEvent, eventMessage);
            }
        }
    }

    private RoomUser getAnotherUser(RoomUser user) {
        final Optional<RoomUser> anotherUserOptional = roomUserMap.values().stream()
                .filter(u ->!u.getUserId().equals(user.getUserId()))
                .findFirst();
        return anotherUserOptional.orElse(null);
    }

}
