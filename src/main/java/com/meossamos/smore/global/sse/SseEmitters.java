package com.meossamos.smore.global.sse;

import com.meossamos.smore.domain.alarm.alarm.dto.SaveAlarmDto;
import com.meossamos.smore.domain.alarm.alarm.service.AlarmService;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.repository.StudyRepository;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import com.meossamos.smore.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
@RequiredArgsConstructor
public class SseEmitters {
    private final AlarmService alarmService;
    private final StudyRepository studyRepository;
    private final TokenProvider tokenProvider;

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter add(String token, SseEmitter emitter) {
        emitterMap.put(token, emitter);
        emitter.onCompletion(() -> emitterMap.remove(token));
        emitter.onTimeout(emitter::complete);
        return emitter;
    }

    public SseEmitter get(String token) {
        return emitterMap.get(token);
    }

    public void noti(String eventName, Map<String, Object> data) {
        emitters.forEach(emitter -> sendNotification(emitter, eventName, data));
    }

    public void notiApplication(String eventName, Map<String, String> map, Long recruitmentId) {
        Long receiverId = Long.valueOf(map.get("receiver"));
        Long senderId = Long.valueOf(tokenProvider.getAuthentication(map.get("senderToken")).getName());
        Study study = studyRepository.findById(Long.valueOf(map.get("studyId"))).orElseThrow();
        String message = map.get("sender") + "님이 " + study.getTitle() + "에 지원하였습니다.";

        saveAlarm(receiverId, senderId, study.getId(), recruitmentId, eventName, message);
        sendToReceivers(receiverId, eventName, study.getId(), recruitmentId);
    }

    public void notiAddStudyMember(Long memberId, Long studyId) {
        Study study = studyRepository.findById(studyId).orElseThrow();
        saveAlarm(memberId, null, studyId, null, "application__permitted", study.getTitle() + " 스터디에 초대되었습니다.");
        sendToReceivers(memberId, "application__permitted", studyId, null);
    }

    public void notiCreateDmRoom(Member sender, Member receiver) {
        String message = sender.getNickname() + "님 과의 1:1 채팅이 시작되었습니다.";
        saveAlarm(receiver.getId(), null, null, null, "dm__created", message);
        sendToReceivers(receiver.getId(), "dm__created", null, null);
    }

    public void notiRejectStudyMember(Member member, Study study) {
        saveAlarm(member.getId(), null, study.getId(), null, "application__rejected", study.getTitle() + "에서 지원을 거절했습니다.");
        sendToReceivers(member.getId(), "application__rejected", study.getId(), null);
    }

    //권한 부여시 알림
    public void notiAddStudyMemberPermission(StudyMember studyMember, String permission){
        Long receiverMemberId = studyMember.getMember().getId();
        Long studyId = studyMember.getStudy().getId();

        StringBuilder permissionName= new StringBuilder();

        switch (permission){
            case "permissionRecruitManage":
                permissionName.append("스터디 모집글 관리 권한");
                break;
            case "permissionArticleManage":
                permissionName.append("스터디 내 게시글 관리 권한");
                break;
            case "permissionCalendarManage":
                permissionName.append("스터디 내 캘린더 관리 권한");
                break;
            case "permissionSettingManage":
                permissionName.append("스터디 내 설정 수정 권한");
                break;
        }

        saveAlarm(receiverMemberId,null,studyId,null,"permission__granted",studyMember.getStudy().getTitle()+"에서 "+permissionName+"을 부여하였습니다.");
        sendToReceivers(receiverMemberId,"permission__granted",studyId,null);
    }

    //권한 삭제시 알림
    public void notiRemoveStudyMemberPermission(StudyMember studyMember, String permission){
        Long receiverMemberId = studyMember.getMember().getId();
        Long studyId = studyMember.getStudy().getId();

        StringBuilder permissionName= new StringBuilder();

        switch (permission){
            case "permissionRecruitManage":
                permissionName.append("스터디 모집글 관리 권한");
                break;
            case "permissionArticleManage":
                permissionName.append("스터디 내 게시글 관리 권한");
                break;
            case "permissionCalendarManage":
                permissionName.append("스터디 내 캘린더 관리 권한");
                break;
            case "permissionSettingManage":
                permissionName.append("스터디 내 설정 수정 권한");
                break;
        }

        saveAlarm(receiverMemberId,null,studyId,null,"permission__removed",studyMember.getStudy().getTitle()+"에서 "+permissionName+"을 삭제하였습니다.");
        sendToReceivers(receiverMemberId,"permission__removed",studyId,null);

    }

    private void saveAlarm(Long receiverId, Long senderId, Long studyId, Long recruitmentId, String eventName, String message) {
        alarmService.saveAlarm(SaveAlarmDto.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .studyId(studyId)
                .recruitmentId(recruitmentId)
                .message(message)
                .eventName(eventName)
                .build());
    }

    private void sendToReceivers(Long receiverId, String eventName, Long studyId, Long recruitmentId) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("eventName", eventName);
        dataMap.put("receiverId", receiverId);
        dataMap.put("studyId", studyId);
        dataMap.put("recruitmentId", recruitmentId);

        findReceiverTokens(receiverId).forEach(token -> sendNotification(emitterMap.get(token), eventName, dataMap));
    }

    private List<String> findReceiverTokens(Long receiverId) {
        List<String> receiverTokens = new ArrayList<>();
        emitterMap.forEach((token, emitter) -> {
            if (tokenProvider.getAuthentication(token).getName().equals(receiverId.toString())) {
                receiverTokens.add(token);
            }
        });
        return receiverTokens;
    }

    private void sendNotification(SseEmitter emitter, String eventName, Map<String, Object> data) {
        if (emitter == null) return;
        try {
            emitter.send(SseEmitter.event().name(eventName).data(data));
        } catch (ClientAbortException e) {
            log.warn("Client disconnected: {}", e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Failed to send SSE notification", e);
        }
    }
}

