package com.meossamos.smore.domain.chat.webMedia.model;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoomUser {
    private String roomId;
    private String userId;

    private boolean published;
    private LocalDateTime joinedAt;

    @JsonIgnore
    private WebSocketSession session;


}
