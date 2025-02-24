package com.meossamos.smore.domain.study.schedule.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import com.meossamos.smore.domain.study.schedule.repository.StudyScheduleRepository;
import com.meossamos.smore.domain.study.study.entity.Study;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyScheduleService {
    private final StudyScheduleRepository studyScheduleRepository;

    public StudySchedule saveStudySchedule(String title, String content, LocalDateTime startDate, LocalDateTime endDate, Member member, Study study) {
        StudySchedule studySchedule = StudySchedule.builder()
                .title(title)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .member(member)
                .study(study)
                .build();

        return studyScheduleRepository.save(studySchedule);
    }
}
