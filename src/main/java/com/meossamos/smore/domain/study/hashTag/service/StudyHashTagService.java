package com.meossamos.smore.domain.study.hashTag.service;

import com.meossamos.smore.domain.study.hashTag.entity.StudyHashTag;
import com.meossamos.smore.domain.study.hashTag.repository.StudyHashTagRepository;
import com.meossamos.smore.domain.study.study.entity.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyHashTagService {
    private final StudyHashTagRepository studyHashTagRepository;

    public StudyHashTag saveStudyHashTag(String hashTag, Study study) {
        StudyHashTag studyHashTag = StudyHashTag.builder()
                .hashTag(hashTag)
                .study(study)
                .build();

        return studyHashTagRepository.save(studyHashTag);
    }
}
