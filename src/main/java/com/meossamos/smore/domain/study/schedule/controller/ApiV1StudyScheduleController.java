package com.meossamos.smore.domain.study.schedule.controller;

import com.meossamos.smore.domain.study.schedule.dto.AddStudyScheduleDto;
import com.meossamos.smore.domain.study.schedule.dto.StudyScheduleDto;
import com.meossamos.smore.domain.study.schedule.dto.UpdateStudyScheduleDto;
import com.meossamos.smore.domain.study.schedule.entity.StudySchedule;
import com.meossamos.smore.domain.study.schedule.repository.StudyScheduleRepository;
import com.meossamos.smore.domain.study.schedule.service.StudyScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1/study")
@RestController
public class ApiV1StudyScheduleController {

    final StudyScheduleRepository studyScheduleRepository;
    final StudyScheduleService studyScheduleService;

    // 다건 조회 & 매니저인지 확인
    @GetMapping("/{study_id}/schedules")
    public ResponseEntity<Map<String, Object>> getSchedules(
            @PathVariable Long study_id,
            @RequestHeader("Authorization") String authorizationHeader
    ){
        // 일정 다건 조회
        List<StudySchedule> StudyScheduleList =  studyScheduleRepository.findByStudy_Id(study_id);
        List<StudyScheduleDto> studyScheduleDtoList = StudyScheduleList.stream()
                .map(StudyScheduleDto::new).toList();
        studyScheduleDtoList.forEach(studyScheduleDto ->
                System.out.println(studyScheduleDto.getContent())
//                System.out.println;

                );

        // 매니저인지 여부
        String accessToken = authorizationHeader.replace("Bearer ", "");
        System.out.println("pathVariable study_id = " + study_id);
        boolean userPermission = studyScheduleService.checkManager(accessToken, study_id);
        Map<String, Object> response = new HashMap<>();
        response.put("userPermission", userPermission);
        response.put("studyScheduleList", studyScheduleDtoList);
        return ResponseEntity.ok(response);
    }

    // 생성
    @PostMapping("/{study_id}/schedules")
    public ResponseEntity<?> addSchedule(@PathVariable Long study_id,
                                                  @RequestBody AddStudyScheduleDto addstudyScheduleDto){
        try {
            StudySchedule studySchedule = studyScheduleService.addStudySchedule(study_id, addstudyScheduleDto);
            System.out.println(addstudyScheduleDto.toString());
            return ResponseEntity.ok(addstudyScheduleDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("스케쥴 생성 실패 " + e.getMessage());
        }
    }

    // 삭제
    @DeleteMapping("/{study_id}/schedules")
    public ResponseEntity<?> deleteSchedule(@RequestBody Map<String, Long> request){
        Long eventId = request.get("id");
        try {
            studyScheduleService.deleteSchedule(eventId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                            .body("스케쥴 삭제 실패 " + e.getMessage());
        }
    }

    // 수정
    @PutMapping("/{study_id}/schedules")
    public ResponseEntity<?> updateSchedule(@RequestBody UpdateStudyScheduleDto updateStudyScheduleDto){
        try {
            studyScheduleService.updateSchedule(updateStudyScheduleDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("스케쥴 수정 실패 " + e.getMessage());
        }
        
    }

}
