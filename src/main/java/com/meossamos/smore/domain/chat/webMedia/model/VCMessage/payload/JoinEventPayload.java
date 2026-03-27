package com.meossamos.smore.domain.chat.webMedia.model.VCMessage.payload;

import com.meossamos.smore.domain.chat.webMedia.model.RoomUser;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class JoinEventPayload {
    private RoomUser user;
}
