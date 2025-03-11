package com.meossamos.smore.domain.study.studyMember.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import com.meossamos.smore.domain.study.hashTag.entity.StudyHashTag;
import com.meossamos.smore.domain.study.study.dto.StudyDto;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.repository.StudyRepository;
import com.meossamos.smore.domain.study.studyMember.dto.StudyWithPositionSimpleDto;
import com.meossamos.smore.domain.study.studyMember.dto.UpdateStudyMemberPermissionDto;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import com.meossamos.smore.domain.study.studyMember.entity.StudyPosition;
import com.meossamos.smore.domain.study.studyMember.repository.StudyMemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyMemberService {
    private final StudyMemberRepository studyMemberRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 멤버 추가 (리더 전용)
     * @param studyId 스터디 ID
     * @param leaderMemberId 요청자(리더)의 ID
     * @param newMemberId 새로 추가할 멤버의 ID
     * @return 생성된 StudyMember 엔티티
     */
    @Transactional
    public StudyMember addMember(Long studyId, Long leaderMemberId, Long newMemberId) {
        // 요청자가 스터디의 리더인지 확인
        StudyMember leaderStudyMember = studyMemberRepository.findByStudyIdAndMemberId(studyId, leaderMemberId)
                .orElseThrow(() -> new IllegalArgumentException("스터디에 등록된 리더 정보가 없습니다."));
        if (leaderStudyMember.getPosition() != StudyPosition.LEADER) {
            throw new IllegalArgumentException("스터디 리더만 멤버를 추가할 수 있습니다.");
        }

        // 추가할 멤버가 이미 스터디에 등록되어 있는지 확인
        if (studyMemberRepository.findByStudyIdAndMemberId(studyId, newMemberId).isPresent()) {
            throw new IllegalArgumentException("해당 멤버는 이미 스터디에 등록되어 있습니다.");
        }

        // 불필요한 DB 조회를 줄이기 위해 getReference 사용
        Study study = entityManager.getReference(Study.class, studyId);
        Member newMember = entityManager.getReference(Member.class, newMemberId);

        // 기본적으로 새 멤버는 MEMBER 직책이며, 권한은 모두 false로 설정
        StudyMember studyMember = StudyMember.builder()
                .study(study)
                .member(newMember)
                .position(StudyPosition.MEMBER)
                .permissionRecruitManage(false)
                .permissionArticleManage(false)
                .permissionCalendarManage(false)
                .permissionSettingManage(false)
                .build();

        return studyMemberRepository.save(studyMember);
    }

    /**
     * 강퇴: 스터디 리더가 특정 멤버를 강퇴함 (자기 자신은 제외)
     * @param studyId 스터디 ID
     * @param leaderMemberId 요청자(리더)의 ID
     * @param targetMemberId 강퇴 대상 멤버의 ID
     */
    @Transactional
    public void removeMember(Long studyId, Long leaderMemberId, Long targetMemberId) {
        // 요청자가 스터디의 리더인지 확인
        StudyMember leaderStudyMember = studyMemberRepository.findByStudyIdAndMemberId(studyId, leaderMemberId)
                .orElseThrow(() -> new IllegalArgumentException("스터디에 등록된 리더 정보가 없습니다."));
        if (leaderStudyMember.getPosition() != StudyPosition.LEADER) {
            throw new IllegalArgumentException("스터디 리더만 멤버를 강퇴할 수 있습니다.");
        }
        // 리더 본인은 강퇴할 수 없음
        if (leaderMemberId.equals(targetMemberId)) {
            throw new IllegalArgumentException("리더는 자기 자신을 강퇴할 수 없습니다.");
        }

        int deletedCount = studyMemberRepository.deleteByStudyIdAndMemberId(studyId, targetMemberId);
        if (deletedCount == 0) {
            throw new IllegalArgumentException("해당 멤버를 스터디에서 찾을 수 없습니다.");
        }
    }

    /**
     * 탈퇴: 스터디 멤버 본인이 탈퇴
     * 단, 리더는 탈퇴할 수 없으므로 별도의 리더 위임 로직이 필요함.
     * @param studyId 스터디 ID
     * @param memberId 탈퇴 요청 멤버의 ID
     */
    @Transactional
    public void leaveStudy(Long studyId, Long memberId) {
        StudyMember studyMember = studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("스터디에 등록된 멤버가 아닙니다."));
        if (studyMember.getPosition() == StudyPosition.LEADER) {
            throw new IllegalArgumentException("리더는 탈퇴할 수 없습니다. 리더 위임 후 탈퇴하세요.");
        }

        int deletedCount = studyMemberRepository.deleteByStudyIdAndMemberId(studyId, memberId);
        if (deletedCount == 0) {
            throw new IllegalArgumentException("멤버 탈퇴에 실패했습니다.");
        }
    }

    /**
     * 권한 변경: 스터디 리더가 특정 멤버의 권한을 변경
     * @param studyId 스터디 ID
     * @param leaderMemberId 요청자(리더)의 ID
     * @param targetMemberId 권한 변경 대상 멤버의 ID
     * @param dto 변경할 권한 정보
     * @return 변경된 StudyMember 엔티티
     */
    @Transactional
    public StudyMember updateMemberPermissions(Long studyId, Long leaderMemberId, Long targetMemberId,
                                               UpdateStudyMemberPermissionDto dto) {
        // 요청자가 스터디의 리더인지 확인
        StudyMember leaderStudyMember = studyMemberRepository.findByStudyIdAndMemberId(studyId, leaderMemberId)
                .orElseThrow(() -> new IllegalArgumentException("스터디에 등록된 리더 정보가 없습니다."));
        if (leaderStudyMember.getPosition() != StudyPosition.LEADER) {
            throw new IllegalArgumentException("스터디 리더만 멤버의 권한을 변경할 수 있습니다.");
        }
        // 리더는 자신의 권한을 변경할 수 없음
        if (leaderMemberId.equals(targetMemberId)) {
            throw new IllegalArgumentException("리더는 자기 자신의 권한을 변경할 수 없습니다.");
        }

        StudyMember targetMember = studyMemberRepository.findByStudyIdAndMemberId(studyId, targetMemberId)
                .orElseThrow(() -> new IllegalArgumentException("대상 멤버를 스터디에서 찾을 수 없습니다."));

        targetMember.setPermissionRecruitManage(dto.getPermissionRecruitManage());
        targetMember.setPermissionArticleManage(dto.getPermissionArticleManage());
        targetMember.setPermissionCalendarManage(dto.getPermissionCalendarManage());
        targetMember.setPermissionSettingManage(dto.getPermissionSettingManage());

        return studyMemberRepository.save(targetMember);
    }

    /**
     * 권한 조회: 특정 멤버의 권한 정보를 조회
     * @param studyId 스터디 ID
     * @param memberId 대상 멤버의 ID
     * @return 해당 StudyMember 엔티티
     */
    @Transactional(readOnly = true)
    public StudyMember getMemberPermissions(Long studyId, Long memberId) {
        return studyMemberRepository.findByStudyIdAndMemberId(studyId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("스터디에 등록된 멤버가 아닙니다."));
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
    /**
     * 멤버 아이디로 해당 멤버가 속한 Study와 position 목록 조회
     * @param memberId 멤버의 ID
     * @return Study와 position을 담은 DTO 리스트
     */
    public List<StudyWithPositionSimpleDto> getStudiesWithPositionByMemberId(Long memberId) {
        return studyMemberRepository.findStudiesWithPositionSimpleByMemberId(memberId);
    }
}
