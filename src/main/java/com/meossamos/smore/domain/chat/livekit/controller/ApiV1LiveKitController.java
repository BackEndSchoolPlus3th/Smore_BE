package com.meossamos.smore.domain.chat.livekit.controller;

import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import io.livekit.server.WebhookReceiver;
import livekit.LivekitWebhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/v1/livekit")
@RestController
public class ApiV1LiveKitController {

    @Value("${livekit.apiKey")
    private String apikey;

    @Value("${livekit.apiSecret}")
    private String apiSecret;

    @GetMapping("/getToken")
    public ResponseEntity<Map<String, String>> getToken(@RequestParam Map<String, String> params) {
        String roomName = params.get("roomName");
        String participantName = params.get("participantName");

        if (roomName == null || participantName == null) {
            return ResponseEntity.badRequest().body(Map.of("errorMessage", "roomName and participantName are required"));
        }

        AccessToken token = new AccessToken(apikey, apiSecret);
        token.setName(participantName);
        token.setIdentity(participantName);
        token.addGrants(new RoomJoin(true), new RoomName(roomName));


        return ResponseEntity.ok(Map.of("token", token.toJwt()));
    }

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
