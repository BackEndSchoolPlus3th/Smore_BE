package com.meossamos.smore.domain.study.schedule.controller;

import com.meossamos.smore.domain.study.schedule.dto.AddStudyScheduleDto;
import com.meossamos.smore.domain.study.schedule.dto.StudyScheduleDto;
import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import com.meossamos.smore.domain.study.schedule.repository.StudyScheduleRepository;
import com.meossamos.smore.domain.study.schedule.service.StudyScheduleService;
import com.meossamos.smore.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/study")
@RestController
public class ApiV1StudyScheduleController {

    final StudyScheduleRepository studyScheduleRepository;
    final StudyScheduleService studyScheduleService;

    // 다건 조회
    @GetMapping("/{study_id}/schedules")
    public RsData<List<StudyScheduleDto>> getSchedules(@PathVariable Long study_id){
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

    // 생성
    @PostMapping("/{study_id}/schedules")
    public RsData<AddStudyScheduleDto> addScedule(@PathVariable Long study_id,
                                                  @RequestBody AddStudyScheduleDto addstudyScheduleDto){

        System.out.println(addstudyScheduleDto);
        return new RsData<>("200","스케쥴 저장 성공", addstudyScheduleDto);
    }


    // 수정

    // 삭제
}
