package com.meossamos.smore.domain.study.study.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.hashTag.entity.StudyHashTag;
import com.meossamos.smore.domain.study.study.dto.StudyDto;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.repository.StudyRepository;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import com.meossamos.smore.domain.study.studyMember.repository.StudyMemberRepository;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final StudyMemberService studyMemberService;
    private final StudyMemberRepository studyMemberRepository;

    public Study saveStudy(String title, Integer memberCnt, @Nullable String imageUrls, @Nullable String introduction, Member leader) {
        Study study = Study.builder()
                .title(title)
                .memberCnt(memberCnt)
                .imageUrls(imageUrls)
                .introduction(introduction)
                .leader(leader)
                .build();

        return studyRepository.save(study);
    }

    // study entity 반환 메서드 (GroupChatRoomService 로직때문에 추가)
    public Study getStudyEntityById(Long studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("스터디를 찾을 수 없습니다."));
    }

    // Dto 변환
    public StudyDto convertToStudyDto(Study study) {

        List<String> hashTags = study.getStudyHashTagList().stream()
                .map(StudyHashTag::getHashTag)
                .collect(Collectors.toList());

        return StudyDto.builder()
                .id(study.getId())
                .title(study.getTitle())
                .introduction(study.getIntroduction())
                .hashTags(hashTags)
                .build();
    }

    // 스터디 정보 조회
    public StudyDto getStudyById(Long studyId) {
        Long memberId = studyMemberService.getAuthenticatedMemberId();

        List<StudyMember> studyMembers = studyMemberRepository.findByMemberId(memberId);

        // Study ID 목록
        List<Long> studyIds = studyMembers.stream()
                .map(studyMember -> studyMember.getStudy().getId())  // Study ID 추출
                .collect(Collectors.toList());

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("해당 ID에 해당하는 스터디가 존재하지 않습니다."));

        // StudyDto 반환
        return convertToStudyDto(study);
    }

    // 스터디 정보 업데이트
    public StudyDto updateStudyIntroductions(Long studyId, StudyDto studyDto) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("스터디를 찾을 수 없습니다."));
        

        List<StudyHashTag> updatedHashTags = studyDto.getHashTags().stream()
                .map(tag -> new StudyHashTag(tag, study)) // StudyHashTag 생성
                .collect(Collectors.toList());

        study.setTitle(studyDto.getTitle());
        study.setIntroduction(studyDto.getIntroduction());
        study.setStudyHashTagList(updatedHashTags);

        Study updatedStudy = studyRepository.save(study);

        return convertToStudyDto(updatedStudy);
    }

    public Study getReferenceById(Long studyId) {
        return studyRepository.getReferenceById(studyId);
    }
}
