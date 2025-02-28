package com.meossamos.smore.domain.chat.dm.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DmRoomResponseDto {
    private Long roomId;
    private Long member1Id;
    private Long member2Id;
    private LocalDateTime timestamp;
}
