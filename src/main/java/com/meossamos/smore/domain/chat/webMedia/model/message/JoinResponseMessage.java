package com.meossamos.smore.domain.chat.webMedia.model.message;

import com.meossamos.smore.domain.chat.webMedia.model.RoomUser;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JoinResponseMessage {
    private String apiUrl;
    private String streamUrl;
    private String roomId;
    private RoomUser user;
    private RoomUser anotherUser;


}

