package com.meossamos.smore.domain.chat.webMedia.model.VCMessage.payload;

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
    public String userId;
}
