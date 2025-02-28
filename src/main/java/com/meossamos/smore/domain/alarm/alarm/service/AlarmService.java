package com.meossamos.smore.domain.alarm.alarm.service;

import com.meossamos.smore.domain.alarm.alarm.entity.Alarm;
import com.meossamos.smore.domain.alarm.alarm.repository.AlarmRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public Alarm saveAlarm(Member receiver, String message, @Nullable Long senderId) {
        Alarm alarm = Alarm.builder()
                .receiver(receiver)
                .message(message)
                .senderId(senderId)
                .build();

        return alarmRepository.save(alarm);
    }
}
