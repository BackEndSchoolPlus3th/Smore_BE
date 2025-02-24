package com.meossamos.smore.domain.study.study.service;

import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;

    public Study saveStudy(String name, String description, String region, String hashTags) {
        Study study = Study.builder()
                .name(name)
                .description(description)
                .region(region)
                .hashTags(hashTags)
                .build();
        return studyRepository.save(study);
    }
}
