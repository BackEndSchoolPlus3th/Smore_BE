package com.meossamos.smore.domain.chat.groupChat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GroupChatRoomDto {
    @JsonProperty("roomId")
    private Long id;
    private Long studyId;
    private String studyName;
    private LocalDateTime createdDate;
}
