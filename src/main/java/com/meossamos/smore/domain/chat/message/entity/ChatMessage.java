package com.meossamos.smore.domain.chat.message.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat_messages") // MongoDB 컬렉션 지정 
public class ChatMessage {

    @Id
    private String id;  // MongoDB는 기본적으로 ObjectId 사용 (String)

    private String roomId;
    private String senderId;
    private String message;
    private String attachment;

    @CreatedDate
    @Field(targetType = FieldType.TIMESTAMP) // MongoDB에서 LocalDateTime 저장
    private LocalDateTime createdDate;
}
