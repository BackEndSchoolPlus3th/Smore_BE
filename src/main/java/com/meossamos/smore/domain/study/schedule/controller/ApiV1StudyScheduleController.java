package com.meossamos.smore.domain.study.schedule.controller;

import com.meossamos.smore.domain.study.schedule.dto.StudyScheduleDto;
import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import com.meossamos.smore.domain.study.schedule.repository.StudyScheduleRepository;
import com.meossamos.smore.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/study")
@RestController
public class ApiV1StudyScheduleController {

    final StudyScheduleRepository studyScheduleRepository;

    // 다건 조회
    @GetMapping("/{study_id}/schedules")
    public RsData<List<StudyScheduleDto>> getSchedules(@PathVariable Long study_id){
//        List<Schedule> scheduleList =
        List<StudySchedule> StudyScheduleList =  studyScheduleRepository.findByStudy_Id(study_id);
        List<StudyScheduleDto> studyScheduleDtoList = StudyScheduleList.stream()
                .map(StudyScheduleDto::new).toList();
        studyScheduleDtoList.forEach(studyScheduleDto ->
                System.out.println(studyScheduleDto.getContent())
                );
        return new RsData<>(
                "200",
        "스케줄 다건 조회",
                studyScheduleDtoList
                );
    }
    // 상세 조회

    // 생성

    // 수정

    // 삭제
}
