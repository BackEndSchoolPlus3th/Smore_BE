package com.meossamos.smore.global.sse;

import com.meossamos.smore.global.jwt.TokenProvider;
import com.meossamos.smore.global.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
@RequiredArgsConstructor
public class SseEmitters {

    private final TokenProvider tokenProvider;
    // Thread-safe한 List를 사용하여 다중 클라이언트의 SSE 연결들을 관리
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<String,SseEmitter> emitterMap = new ConcurrentHashMap<String, SseEmitter>();
    // 새로운 SSE 연결을 추가하고 관련 콜백을 설정하는 메서드
    public SseEmitter add(SseEmitter emitter) {
        this.emitters.add(emitter);

        // 클라이언트와의 연결이 완료되면 컬렉션에서 제거하는 콜백
        emitter.onCompletion(() -> {
            this.emitters.remove(emitter);
        });

        // 연결이 타임아웃되면 완료 처리하는 콜백
        emitter.onTimeout(() -> {
            emitter.complete();
        });

        return emitter;
    }
    public SseEmitter add(String token,SseEmitter emitter) {
        this.emitterMap.put(token,emitter);

        // 클라이언트와의 연결이 완료되면 컬렉션에서 제거하는 콜백
        emitter.onCompletion(() -> {
            this.emitterMap.remove(token);
        });

        // 연결이 타임아웃되면 완료 처리하는 콜백
        emitter.onTimeout(() -> {
            emitter.complete();
        });

        return emitter;
    }
    public SseEmitter get(String token) {
        return this.emitterMap.get(token);
    }
    // 데이터 없이 이벤트 이름만으로 알림을 보내는 간편 메서드
    public void noti(String eventName) {
        noti(eventName, Ut.mapOf()); // 빈 Map으로 알림 전송
    }

    // 모든 연결된 클라이언트들에게 이벤트를 전송하는 메서드
    public void noti(String eventName, Map<String, Object> data) {
        // 모든 emitter에 대해 반복하며 이벤트 전송
        emitters.forEach(emitter -> {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name(eventName)    // 이벤트 이름 설정
                                .data(data)         // 전송할 데이터 설정
                );
            } catch (ClientAbortException e) {
                // 클라이언트가 연결을 강제로 종료한 경우 무시
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void notiApplication(String eventName, Map<String,String> map,Long recruitmentId){
        String receiverId = map.get("receiver");
        String senderToken = map.get("sender");// 토큰

        Authentication authentication = tokenProvider.getAuthentication(senderToken);
        Long senderId = Long.valueOf(authentication.getName());
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("실제 아이디 인지 확인 {}",userDetails.getUsername());
        //받아서 receiverId를 포함한 토큰을 찾기
        //여러개면 여러개 전송
        log.info("notiApplication 함수 ",recruitmentId);

        List<String> receiverIdList = new ArrayList<>();
        for(String token : emitterMap.keySet()){
            log.info("보내는 토큰 {}",token);
            log.info("보내는 토큰 {} ::::: 받는이 아이디 {}",tokenProvider.getAuthentication(token).getName(),receiverId);
            if(tokenProvider.getAuthentication(token).getName().equals(receiverId)){
                receiverIdList.add(token);
            }
            log.info("map 에 있는 토큰들 {} ",token);

        }

        Map<String,Long> dataMap = new HashMap<>();
        dataMap.put("senderId",senderId);
        dataMap.put("studyId",10814L);
        dataMap.put("recruitmentId",recruitmentId);
        for(String token: receiverIdList){
            SseEmitter emitter = emitterMap.get(token);
            log.info("emitter에서 요청한 토큰에 맞게 emitter를 찾아내는지 {} ",token);
            try {
                emitter.send(
                        SseEmitter.event()
                                .name(eventName)    // 이벤트 이름 설정
                                .data(dataMap)         // 전송할 데이터 설정
                );
            } catch (ClientAbortException e) {
                // 클라이언트가 연결을 강제로 종료한 경우 무시
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //토큰으로 emitter 찾기
        //찾은 emitter에 send 하기


    }
}