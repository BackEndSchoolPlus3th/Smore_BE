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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;
    private final MemberService memberService;

    @PostMapping()
    public String saveAlarm(@RequestBody SaveAlarmDto saveAlarmDto){
        alarmService.saveAlarm(saveAlarmDto);
        return "save completed";
    }
    @GetMapping("/{memberId}")
    public ResponseEntity<List<AlarmDto>> getAlarms (@PathVariable("memberId") Long memberId){
        Member member = memberService.findById(memberId);
        List<Alarm> alarms = member.getAlarmList();

        List<AlarmDto> alarmDtos = alarms.stream().map(i->{
            try{
                return AlarmDto.builder()
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
}
