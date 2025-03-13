package com.meossamos.smore.domain.study.study.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.dto.StudyDto;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.repository.StudyRepository;
import com.meossamos.smore.domain.study.studyMember.repository.StudyMemberRepository;
import com.meossamos.smore.domain.study.studyMember.service.StudyMemberService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final StudyRepository studyRepository;
    private final StudyMemberService studyMemberService;
    private final StudyMemberRepository studyMemberRepository;
    private final RedisTemplate<String, Object> redisTemplate;

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

        // StudyDto 객체를 빌드하여 반환
        return StudyDto.builder()
                .id(study.getId())
                .title(study.getTitle())
                .imageUrls(study.getImageUrls())
                .introduction(study.getIntroduction())
                .hashTags(study.getHashTags())  // hashTags 리스트를 전달
                .build();
    }

    // 스터디 목록 조회
    @Cacheable(value = "study", key = "#studyId")
    public StudyDto getStudyById(Long studyId) {
        Long memberId = studyMemberService.getAuthenticatedMemberId();

        Study study = studyRepository.findByIdWithMembers(studyId, memberId)
                .orElseThrow(() -> new RuntimeException("해당 ID에 해당하는 스터디가 존재하지 않습니다."));

        // StudyDto 반환
        return convertToStudyDto(study);
    }

    // 스터디 정보 업데이트
    @CachePut(value = "study", key = "#studyId")
    public StudyDto updateStudyIntroductions(Long studyId, StudyDto studyDto) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("스터디를 찾을 수 없습니다."));

        // 필드 업데이트
        study.setTitle(studyDto.getTitle());
        study.setIntroduction(studyDto.getIntroduction());
        study.setHashTags(studyDto.getHashTags());

        Study updatedStudy = studyRepository.save(study);
        return convertToStudyDto(updatedStudy);
    }

    public Study getReferenceById(Long studyId) {

        return studyRepository.getReferenceById(studyId);
    }
    public List<Study> findStudiesWithGroupChatRoom(List<Long> studyIds) {
        return studyRepository.findStudiesWithGroupChatRoom(studyIds);
    }
}
