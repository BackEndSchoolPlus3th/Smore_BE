package com.meossamos.smore.domain.study.chat.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@Document(collation = "study_chat")
public class StudyChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String studyId;
    private String userId;
    private String message;
}
