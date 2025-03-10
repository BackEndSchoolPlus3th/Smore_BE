package com.meossamos.smore.domain.alarm.alarm.service;

import com.meossamos.smore.domain.alarm.alarm.dto.SaveAlarmDto;
import com.meossamos.smore.domain.alarm.alarm.entity.Alarm;
import com.meossamos.smore.domain.alarm.alarm.repository.AlarmRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    public Alarm saveAlarm(Member receiver, String message, @Nullable Long senderId) {
        Alarm alarm = Alarm.builder()
                .receiver(receiver)
                .message("기본 메시지")
                .senderId(senderId)
                .build();

        return alarmRepository.save(alarm);
    }

    public Alarm saveAlarm(SaveAlarmDto saveAlarmDto) {
        Alarm alarm = Alarm.builder()
                .senderId(saveAlarmDto.getSenderId())
                .receiver(memberRepository.findById(saveAlarmDto.getReceiverId()).get())
                .message(saveAlarmDto.getMessage())
                .senderId(saveAlarmDto.getSenderId())
                .eventName(saveAlarmDto.getEventName())
                .studyId(saveAlarmDto.getStudyId())
                .recruitmentId(saveAlarmDto.getRecruitmentId())
                .build();

        return alarmRepository.save(alarm);
    }

    public List<Alarm> findAllByMemberId(Long alarmId){
        return alarmRepository.findAllByMemberId(alarmId);
    }
}
