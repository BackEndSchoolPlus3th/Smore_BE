package com.meossamos.smore.domain.chat.dm.controller;

import com.meossamos.smore.domain.chat.dm.dto.DmRoomRequestDto;
import com.meossamos.smore.domain.chat.dm.dto.DmRoomResponseDto;
import com.meossamos.smore.domain.chat.dm.entity.DmRoom;
import com.meossamos.smore.domain.chat.dm.service.DmRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms/dm")
@RequiredArgsConstructor
public class DmController {
    private final DmRoomService dmRoomService;

    // DM 채팅방 생성
    @PostMapping
    public ResponseEntity<DmRoomResponseDto> createDmRoom(@RequestBody DmRoomRequestDto requestDto) {
        return ResponseEntity.ok(dmRoomService.createDmRoom(
                requestDto.getMember1Id(), requestDto.getMember2Id()
        ));
    }

    // DM 채팅방 목록 조회
    @GetMapping
    public ResponseEntity<List<DmRoomResponseDto>> getDmRoomList(@RequestParam("memberId") Long memberId) {
        return ResponseEntity.ok(dmRoomService.getUserDmRooms(memberId));
    }

    // DM 채팅방 삭제
    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteChatRoom(@PathVariable("roomId") Long roomId) {
        dmRoomService.deleteDmRoom(roomId);
        return ResponseEntity.ok("Deleted chat room with id " + roomId);
    }
}
