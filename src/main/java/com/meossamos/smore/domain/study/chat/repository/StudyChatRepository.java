package com.meossamos.smore.domain.study.chat.repository;

import com.meossamos.smore.domain.study.chat.entity.StudyChat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudyChatRepository extends MongoRepository<StudyChat, String> {
}
