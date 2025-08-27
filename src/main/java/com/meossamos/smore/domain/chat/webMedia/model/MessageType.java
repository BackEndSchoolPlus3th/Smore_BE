package com.meossamos.smore.domain.chat.webMedia.model;

public enum MessageType {
    JoinRequest,

    JoinResponse,
    ErrorResponse,

    UserPublishedChangeReport,

    UserJoinedEvent,
    UserLeftEvent,
    UserStateChangedEvent;

}
