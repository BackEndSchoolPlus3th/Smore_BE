package com.meossamos.smore.domain.chat.webMedia.model.message;

import com.meossamos.smore.domain.chat.webMedia.model.RoomUser;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserJoinedEventMessage {
    private RoomUser user;
}
