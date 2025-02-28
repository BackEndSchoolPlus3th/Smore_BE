package com.meossamos.smore.domain.study.study.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.dto.StudyDto;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.repository.StudyRepository;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import com.meossamos.smore.domain.study.studyMember.repository.StudyMemberRepository;
import com.meossamos.smore.domain.study.hashTag.entity.StudyHashTag;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
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

    // Dto 변환
    private StudyDto convertToStudyDto(Study study) {

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

    // 유저 스터디 목록 조회
    public List<StudyDto> getStudiesForMember(Member member) {
        List<StudyMember> studyMembers = studyMemberRepository.findByMember(member);

        return studyMembers.stream()
                .map(studyMember -> convertToStudyDto(studyMember.getStudy()))
                .collect(Collectors.toList());
    }

    // 스터디 정보 조회
    public StudyDto getStudyById(Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("스터디를 찾을 수 없습니다."));

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
}
