package com.meossamos.smore.domain.study.studyMember.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import com.meossamos.smore.domain.study.hashTag.entity.StudyHashTag;
import com.meossamos.smore.domain.study.study.dto.StudyDto;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.repository.StudyRepository;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import com.meossamos.smore.domain.study.studyMember.repository.StudyMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyMemberService {
    private final StudyMemberRepository studyMemberRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    public StudyMember saveStudyMember(Member member, Study study, Boolean permissionRecruitManage, Boolean permissionArticleManage, Boolean permissionCalendarManage, Boolean permissionSettingManage) {
        StudyMember studyMember = StudyMember.builder()
                .member(member)
                .study(study)
                .permissionRecruitManage(permissionRecruitManage)
                .permissionArticleManage(permissionArticleManage)
                .permissionCalendarManage(permissionCalendarManage)
                .permissionSettingManage(permissionSettingManage)
                .build();

        return studyMemberRepository.save(studyMember);
    }

    @Transactional
    public StudyMember saveStudyMember2(Long memberId, Long studyId, Boolean permissionRecruitManage, Boolean permissionArticleManage, Boolean permissionCalendarManage, Boolean permissionSettingManage) {
        // Retrieve the Member and Study objects based on the provided IDs
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found with id: " + studyId));

        // Create a new StudyMember object and save it to the repository
        StudyMember studyMember = StudyMember.builder()
                .member(member)
                .study(study)
                .permissionRecruitManage(permissionRecruitManage)
                .permissionArticleManage(permissionArticleManage)
                .permissionCalendarManage(permissionCalendarManage)
                .permissionSettingManage(permissionSettingManage)
                .build();

        return studyMemberRepository.save(studyMember);
    }

    // 유저 탈퇴
    public void leaveStudy(Member member, Long studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("스터디를 찾을 수 없습니다."));

        StudyMember studyMember = studyMemberRepository.findByMemberAndStudy(member, study)
                .orElseThrow(() -> new RuntimeException("해당 스터디에 가입되어 있지 않습니다."));

        studyMemberRepository.delete(studyMember);

    }

    // 유저의 스터디 목록 조회 (스터디 이름, 소개, 해시태그 포함)
    @Transactional
    public List<StudyDto> getStudiesByAuthenticatedUser() {
        Long memberId = getAuthenticatedMemberId();

        // 사용자 ID로 관련된 Study ID 조회
        List<StudyMember> studyMembers = studyMemberRepository.findByMemberId(memberId);

        // Study 목록 반환
        List<Long> studyIds = studyMembers.stream()
                .map(studyMember -> studyMember.getStudy().getId())  // Study ID 추출
                .collect(Collectors.toList());

        // Study 정보 조회
        List<Study> studies = studyRepository.findByIdIn(studyIds);

        // StudyDto로 변환
        return studies.stream()
                .map(study -> new StudyDto(
                        study.getId(),  // ID 포함
                        study.getTitle(),
                        study.getIntroduction(),
                        study.getStudyHashTagList().stream()
                                .map(StudyHashTag::getHashTag)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    // 유저의 스터디 목록 조회
    @Transactional
    public List<Long> getStudyListByAuthenticatedUser() {
        Long memberId = getAuthenticatedMemberId();

        // 사용자 ID로 관련된 Study ID 조회
        List<StudyMember> studyMembers = studyMemberRepository.findByMemberId(memberId);

        // Study ID 목록을 추출
        List<Long> studyIds = studyMembers.stream()
                .map(studyMember -> studyMember.getStudy().getId())
                .distinct()
                .collect(Collectors.toList());

        return studyIds;
    }

    // 로그인 한 유저의 id 확인
    public Long getAuthenticatedMemberId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            User user = (User) principal;
            try {
                return Long.parseLong(user.getUsername());
            } catch (NumberFormatException e) {
                throw new RuntimeException("User ID could not be parsed from username.", e);
            }
        } else {
            throw new RuntimeException("User is not authenticated or principal is not an instance of User.");
        }
    }
}
