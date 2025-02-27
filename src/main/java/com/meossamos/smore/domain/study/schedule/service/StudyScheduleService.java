package com.meossamos.smore.domain.study.schedule.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import com.meossamos.smore.domain.study.schedule.dto.AddStudyScheduleDto;
import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import com.meossamos.smore.domain.study.schedule.repository.StudyScheduleRepository;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.repository.StudyRepository;
import com.meossamos.smore.global.exception.MemberNotFoundException;
import com.meossamos.smore.global.exception.StudyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyScheduleService {
    
    private final StudyScheduleRepository studyScheduleRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;


    // 테스트용 일정 저장
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

    // 작성자 가져와서 member에 집어넣기
    // 일정 생성
    public StudySchedule addStudySchedule(Long study_id, AddStudyScheduleDto addstudyScheduleDto) {

        Study study = studyRepository.findById(study_id).orElseThrow(() ->
                new StudyNotFoundException("Study not found with ID: " + study_id)
        );

        Member member = memberRepository.findById(1L).orElseThrow(() ->
                new MemberNotFoundException("Member not found with ID: " + "member_Id")
        );


        StudySchedule studySchedule = StudySchedule.builder()
                .title(addstudyScheduleDto.getTitle())
                .content(addstudyScheduleDto.getContent())
                .startDate(addstudyScheduleDto.getStartDate())
                .endDate(addstudyScheduleDto.getEndDate())
                .member(member)
                .study(study)
                .build();

        if (studySchedule == null) {
            throw new RuntimeException("Schedule creation failed.");
        }
                                
        return studyScheduleRepository.save(studySchedule);
    }
}
