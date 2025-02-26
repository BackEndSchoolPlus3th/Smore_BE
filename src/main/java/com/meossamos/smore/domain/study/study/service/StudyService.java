package com.meossamos.smore.domain.study.study.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.repository.StudyRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;

    public Study saveStudy(String title, Integer memberCnt, @Nullable String imageUrls, @Nullable String introduction, Member leader) {
        Study study = Study.builder()
                .title(title)
                .memberCnt(memberCnt)
                .imageUrls(imageUrls)
                .introduction(introduction)
                .leader(leader)
                .build();

        return studyRepository.save(study);
    }
}
