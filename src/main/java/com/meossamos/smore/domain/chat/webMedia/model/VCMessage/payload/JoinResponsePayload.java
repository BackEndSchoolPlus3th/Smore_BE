package com.meossamos.smore.domain.chat.webMedia.model.VCMessage.payload;

import com.meossamos.smore.domain.chat.webMedia.model.RoomUser;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JoinResponsePayload {
    public String apiUrl;
    public String streamUrl;
    private String roomId;
    private RoomUser user;
    private RoomUser anotherUser;
}
