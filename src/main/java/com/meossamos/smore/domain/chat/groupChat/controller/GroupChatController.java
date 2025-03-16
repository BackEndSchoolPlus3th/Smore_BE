package com.meossamos.smore.domain.chat.groupChat.controller;

import com.meossamos.smore.domain.chat.groupChat.dto.GroupChatRoomDto;
import com.meossamos.smore.domain.chat.groupChat.entity.GroupChatRoom;
import com.meossamos.smore.domain.chat.groupChat.service.GroupChatRoomService;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.domain.study.studyMember.dto.ParticipantDto;
import com.meossamos.smore.domain.study.studyMember.dto.StudyMemberDto;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chatrooms/group")
@RequiredArgsConstructor
@Slf4j
public class GroupChatController {
    private final GroupChatRoomService groupChatRoomService;
    private final StudyService studyService;
    private final StudyMemberService studyMemberService;
    private final MemberService memberService; // 현재 로그인한 멤버 정보 제공

    // 특정 스터디의 그룹 채팅방 생성 또는 기존 채팅방 반환
    @PostMapping("/{studyId}")
    public ResponseEntity<GroupChatRoomDto> createGroupChatRoom(@PathVariable("studyId") Long studyId) {
        Study study = studyService.getStudyEntityById(studyId);
        GroupChatRoom room = groupChatRoomService.createOrGetGroupChatRoom(study);
        GroupChatRoomDto dto = new GroupChatRoomDto(
                room.getId(),
                study.getId(),
                study.getTitle(),
                room.getCreatedDate()
        );
        return ResponseEntity.ok(dto);
    }

    // 현재 로그인한 사용자가 가입한 스터디의 그룹 채팅방 목록 조회
    @GetMapping
    public ResponseEntity<List<GroupChatRoomDto>> getGroupChatRooms(Principal principal) {
        Member currentMember;
        if (principal == null) {
            // 인증 스킵 시, 테스트용으로 ID 1번 회원을 사용
            currentMember = memberService.findById(1L);
        } else {
            currentMember = memberService.findById(Long.valueOf(principal.getName()));
        }

        // 2. 현재 멤버가 참여 중인 스터디 목록 조회
        List<Long> studyIds = studyMemberService.getStudyListByAuthenticatedUser();
        if (studyIds.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        // 배치 조회: studyIds에 해당하는 Study와 연관된 groupChatRoom을 한 번에 조회
        List<Study> studies = studyService.findStudiesWithGroupChatRoom(studyIds);

        // 3. 각 스터디에 대해 그룹 채팅방(없으면 생성) 조회 및 DTO 변환
        List<GroupChatRoomDto> dtos = new ArrayList<>();
        for (Study study : studies) {
            GroupChatRoom room = study.getGroupChatRoom();
            // 만약 스터디에 그룹 채팅방이 없다면 생성
            if (room == null) {
                room = groupChatRoomService.createOrGetGroupChatRoom(study);
            }
            dtos.add(new GroupChatRoomDto(
                    room.getId(),
                    study.getId(),
                    study.getTitle(),
                    room.getCreatedDate()
            ));
        }
        return ResponseEntity.ok(dtos);
    }

    // 특정 스터디의 멤버 목록 조회
    @GetMapping("/{studyId}/users")
    public ResponseEntity<List<ParticipantDto>> getGroupChatRoomUsers(@PathVariable("studyId") Long studyId) {
        List<ParticipantDto> participantList = studyMemberService.getParticipantsByStudyId(studyId);
        return ResponseEntity.ok(participantList);
    }

}
