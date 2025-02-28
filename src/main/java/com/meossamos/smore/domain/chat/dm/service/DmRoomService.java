package com.meossamos.smore.domain.chat.dm.service;

import com.meossamos.smore.domain.chat.dm.dto.DmRoomResponseDto;
import com.meossamos.smore.domain.chat.dm.entity.DmRoom;
import com.meossamos.smore.domain.chat.dm.repository.DmRoomRepository;
import com.meossamos.smore.domain.chat.message.repository.ChatMessageRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DmRoomService {
    private final DmRoomRepository dmRoomRepository; // 생성자 주입
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 1:1 채팅방 생성
    @Transactional
    public DmRoomResponseDto createDmRoom(Long member1Id, Long member2Id) {
        Member member1 = memberRepository.findById(member1Id)
                .orElseThrow(() -> new IllegalArgumentException("Member1 not found"));
        Member member2 = memberRepository.findById(member2Id)
                .orElseThrow(() -> new IllegalArgumentException("Member2 not found"));

        // 기존 1:1 채팅방이 있는지 확인 (중복 생성 방지)
        DmRoom dmRoom = dmRoomRepository.findByMembers(member1, member2)
                .orElseGet(() -> dmRoomRepository.save(DmRoom.builder()
                        .member1(member1)
                        .member2(member2)
                        .build()));

        // 응답 DTO 생성 및 매핑
        DmRoomResponseDto responseDto = new DmRoomResponseDto();
        responseDto.setRoomId(dmRoom.getId());
        responseDto.setMember1Id(member1.getId());
        responseDto.setMember2Id(member2.getId());
        responseDto.setTimestamp(dmRoom.getCreatedDate());
        return responseDto;
    }

    // 사용자가 속한 모든 DM 채팅방 조회
    public List<DmRoomResponseDto> getUserDmRooms(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        List<DmRoom> rooms = dmRoomRepository.findByMember1OrMember2(member, member);

        // 엔티티 List를 DTO List로 변환
        return rooms.stream().map(dmRoom -> {
            DmRoomResponseDto dto = new DmRoomResponseDto();
            dto.setRoomId(dmRoom.getId());
            dto.setMember1Id(dmRoom.getMember1().getId());
            dto.setMember2Id(dmRoom.getMember2().getId());
            dto.setTimestamp(dmRoom.getCreatedDate());
            return dto;
        }).collect(Collectors.toList());
    }

    // 1:1 채팅방 삭제 (연관된 메시지도 함께 삭제)
    @Transactional
    public void deleteDmRoom(Long roomId) {
        DmRoom dmRoom = dmRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("DM room not found"));

        // 채팅방에 속한 모든 메시지 삭제 (MongoDB)
        chatMessageRepository.deleteByRoomId(roomId.toString());

        // 채팅방 삭제 (JPA)
        dmRoomRepository.delete(dmRoom);
    }
}
