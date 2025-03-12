package com.meossamos.smore.domain.alarm.alarm.controller;


import com.meossamos.smore.domain.alarm.alarm.dto.AlarmDto;
import com.meossamos.smore.domain.alarm.alarm.dto.SaveAlarmDto;
import com.meossamos.smore.domain.alarm.alarm.entity.Alarm;
import com.meossamos.smore.domain.alarm.alarm.service.AlarmService;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> saveAlarm(@RequestBody SaveAlarmDto saveAlarmDto){
        alarmService.saveAlarm(saveAlarmDto);
        return ResponseEntity.ok("저장 완료");
    }
    @GetMapping
    public ResponseEntity<List<AlarmDto>> getAlarms (@AuthenticationPrincipal UserDetails userDetails
    ){

        Long memberId = Long.valueOf(userDetails.getUsername());
        Member member = memberService.findById(memberId);
        List<Alarm> alarms = member.getAlarmList();

        List<AlarmDto> alarmDtos = alarms.stream().map(i->{
            try{
                return AlarmDto.builder()
                        .id(i.getId())
                        .eventName(i.getEventName())
                        .senderId(i.getSenderId())
                        .receiverId(i.getReceiver().getId())
                        .studyId(i.getStudyId())
                        .message(i.getMessage())
                        .recruitmentId(i.getRecruitmentId())
                        .build();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }).toList();

        return new ResponseEntity<>(alarmDtos, HttpStatus.OK);
    }

    @DeleteMapping("/{alarmId}")
    public ResponseEntity<?> delete(@PathVariable("alarmId") Long alarmId){
        alarmService.deleteById(alarmId);
        return ResponseEntity.ok("삭제 완료");
    }
}
