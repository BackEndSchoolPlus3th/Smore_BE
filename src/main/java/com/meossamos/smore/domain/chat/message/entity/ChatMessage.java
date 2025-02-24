package com.meossamos.smore.domain.chat.message.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ChatMessage {

    @Id
    private String id;

    private String roomId;
    private String senderId;
    private String message;
    private String attachment;

    @CreatedDate
    private LocalDateTime createdDate;
}
