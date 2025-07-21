package com.meossamos.smore.domain.chat.webMedia.model.message;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPublishedChangeReportMessage {
    private boolean published;

}
