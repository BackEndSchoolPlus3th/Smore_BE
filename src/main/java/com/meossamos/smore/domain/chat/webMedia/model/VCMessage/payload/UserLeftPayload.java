package com.meossamos.smore.domain.chat.webMedia.model.VCMessage.payload;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserLeftPayload {
    private String userId;
}
