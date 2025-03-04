package com.meossamos.smore.domain.study.schedule.controller;

import com.meossamos.smore.domain.study.schedule.dto.AddStudyScheduleDto;
import com.meossamos.smore.domain.study.schedule.dto.StudyScheduleDto;
import com.meossamos.smore.domain.study.schedule.dto.UpdateStudyScheduleDto;
import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import com.meossamos.smore.domain.study.schedule.repository.StudyScheduleRepository;
import com.meossamos.smore.domain.study.schedule.service.StudyScheduleService;
import com.meossamos.smore.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
//        System.out.println();
        return new RsData<>(
                "200",
        "스케줄 다건 조회",
                studyScheduleDtoList
                );
    }

    // 생성
    @PostMapping("/{study_id}/schedules")
    public RsData<AddStudyScheduleDto> addSchedule(@PathVariable Long study_id,
                                                  @RequestBody AddStudyScheduleDto addstudyScheduleDto){
        try {
            StudySchedule studySchedule = studyScheduleService.addStudySchedule(study_id, addstudyScheduleDto);
            System.out.println(addstudyScheduleDto.toString());
            return new RsData<>("200","스케쥴 저장 성공", addstudyScheduleDto);
        } catch (RuntimeException e) {
            return new RsData<>("500", "스케쥴 저장 실패" + e.getMessage(), null);
        }
    }

    // 삭제
    @DeleteMapping("/{study_id}/schedules")
    public ResponseEntity<RsData> deleteSchedule(@RequestBody Map<String, Long> request){
        Long eventId = request.get("id");
        try {
            studyScheduleService.deleteSchedule(eventId);
            return ResponseEntity.ok(new RsData<>("200", "스케쥴 삭제 성공", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("400"," 스케쥴 삭제 실패 " + e.getMessage(), null));
        }
    }

    // 수정
    @PutMapping("/{study_id}/schedules")
    public ResponseEntity<RsData> updateSchedule(@RequestBody UpdateStudyScheduleDto updateStudyScheduleDto){
        try {
            studyScheduleService.updateSchedule(updateStudyScheduleDto);
            return ResponseEntity.ok(new RsData<>("200", "스케쥴 수정 성공", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RsData<>("400"," 스케쥴 수정 실패 " + e.getMessage(), null));
        }
        
    }

}
