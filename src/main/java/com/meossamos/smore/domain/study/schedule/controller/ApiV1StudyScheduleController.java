package com.meossamos.smore.domain.study.schedule.controller;

import com.meossamos.smore.domain.study.schedule.dto.StudyScheduleDto;
import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import com.meossamos.smore.domain.study.schedule.repository.StudyScheduleRepository;
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
    public List<StudyScheduleDto> getSchedules(@PathVariable Long study_id){
//        List<Schedule> scheduleList =
        List<StudySchedule> StudyScheduleList =  studyScheduleRepository.findByStudy_Id(study_id);
        List<StudyScheduleDto> StudyScheduleDtoList = StudyScheduleList.stream()
                .map(StudyScheduleDto::new).toList();
        return StudyScheduleDtoList;
    }
    // 상세 조회

    // 생성

    // 수정

    // 삭제
}
