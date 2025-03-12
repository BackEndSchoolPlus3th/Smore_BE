package com.meossamos.smore.domain.chat.groupChat.controller;

import com.meossamos.smore.domain.chat.groupChat.dto.GroupChatRoomDto;
import com.meossamos.smore.domain.chat.groupChat.entity.GroupChatRoom;
import com.meossamos.smore.domain.chat.groupChat.service.GroupChatRoomService;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.service.StudyService;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
        List<Long> studyDtos = studyMemberService.getStudyListByAuthenticatedUser();

        // 3. 각 스터디에 대해 그룹 채팅방(없으면 생성) 조회 및 DTO 변환
        List<GroupChatRoomDto> dtos = studyDtos.stream().map(studyDto -> {
            Study study = studyService.getStudyEntityById(studyDto);
            GroupChatRoom room = groupChatRoomService.createOrGetGroupChatRoom(study);
            return new GroupChatRoomDto(
                    room.getId(),
                    study.getId(),
                    study.getTitle(),
                    room.getCreatedDate()
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

}
