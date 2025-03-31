package com.meossamos.smore.global.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Controller
@RequestMapping("/sse")
@RequiredArgsConstructor
@Slf4j
public class SseController {
    // SSE 연결들을 관리하는 컴포넌트
    private final SseEmitters sseEmitters;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@RequestParam("token") String token) {
        // 새로운 SSE 연결 생성 (기본 타임아웃 30초)
        SseEmitter emitter = new SseEmitter();
        log.info("sse 서버 연결 시도 토큰????? {}",token);
        // 생성된 emitter를 컬렉션에 추가하여 관리
        // 이거 나중에 토큰에서 id 찾는게 더 힘드려나??
        sseEmitters.add(token,emitter);

        try {
            // 연결된 클라이언트에게 초기 연결 성공 메시지 전송
            emitter.send(SseEmitter.event()
                    .name("connect")    // 이벤트 이름을 "connect"로 지정
                    .data("connected!")); // 전송할 데이터
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(emitter);
    }
}
