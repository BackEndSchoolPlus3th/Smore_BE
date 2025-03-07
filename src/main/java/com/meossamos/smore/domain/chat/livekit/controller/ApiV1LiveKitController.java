package com.meossamos.smore.domain.chat.livekit.controller;

import com.meossamos.smore.domain.chat.livekit.service.LiveKitService;
import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import io.livekit.server.WebhookReceiver;
import livekit.LivekitWebhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RequiredArgsConstructor
@RestController
public class ApiV1LiveKitController {

    private final LiveKitService liveKitService;

    @Value("${livekit.apiKey}")
    private String apikey;

    @Value("${livekit.apiSecret}")
    private String apiSecret;


    @PostMapping("/api/v1/token")
    public ResponseEntity<Map<String, String>> getToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        // 토큰에서 스터디 이름, 참여자 아이디 가져오기
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            System.out.println(authHeader);
            String jwttoken = authHeader.substring(7); // "Bearer " 이후의 부분을 추출
            // 토큰 복호화해서 유저정보 리턴
//            System.out.println(token);
            Map<String, String> userInfo =liveKitService.getUserInfo(jwttoken);
            String studyTitle = userInfo.get("studyTitle");
//            String userId = userInfo.get("userId");
            String userEmail = userInfo.get("userEmail");


            AccessToken token = new AccessToken(apikey, apiSecret);
            token.setName(userEmail);
            token.setIdentity(userEmail);
            token.addGrants(new RoomJoin(true), new RoomName(studyTitle));

            System.out.println(token.toString());
            System.out.println(studyTitle + userEmail);
            return ResponseEntity.ok(Map.of(
                    "token", token.toJwt(),
                    "StudyTitle", studyTitle,
//                    "UserId", userId,
                    "UserEmail", userEmail));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("errorMessage", "Authorization token is required"));
        }
    }

//        if (roomName == null || participantName == null) {
//            return ResponseEntity.badRequest().body(Map.of("errorMessage", "roomName and participantName are required"));
//        }
//
//        AccessToken token = new AccessToken(apikey, apiSecret);
//        token.setName(participantName);
//        token.setIdentity(participantName);
//        token.addGrants(new RoomJoin(true), new RoomName(roomName));
//
//        System.out.println(token.toString());
//        return ResponseEntity.ok(Map.of("token", token.toJwt()));
//    }

    @PostMapping(value = "/webhook", consumes = "application/webhook+json")
    public ResponseEntity<String> receiveWebhook(@RequestHeader("Authorization") String authHeader, @RequestBody String body) {
        WebhookReceiver webhookReceiver = new WebhookReceiver(apikey, apiSecret);
        try {
            LivekitWebhook.WebhookEvent event = webhookReceiver.receive(body, authHeader);
            System.out.println("LiveKit Webhook: " + event.toString());
        } catch (Exception e) {
            System.err.println("Error validating webhook event: " + e.getMessage());
        }
        return ResponseEntity.ok("ok");
    }
}
