package com.meossamos.smore.domain.study.chat.service;

import com.meossamos.smore.domain.study.chat.entity.StudyChat;
import com.meossamos.smore.domain.study.chat.repository.StudyChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyChatService {
    private final StudyChatRepository studyChatRepository;

    public StudyChat saveStudyChat(StudyChat studyChat) {
        return studyChatRepository.save(studyChat);
    }
}
