package com.meossamos.smore.domain.study.studyMember.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import com.meossamos.smore.domain.study.hashTag.entity.StudyHashTag;
import com.meossamos.smore.domain.study.study.dto.StudyDto;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.study.repository.StudyRepository;
import com.meossamos.smore.domain.study.studyMember.dto.StudyWithPositionSimpleDto;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import com.meossamos.smore.domain.study.studyMember.entity.StudyPosition;
import com.meossamos.smore.domain.study.studyMember.repository.StudyMemberRepository;
import com.meossamos.smore.global.sse.SseEmitters;
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
    private final SseEmitters emitters;
    /**
     * 회원 아이디와 스터디 아이디를 받아 해당 회원이 해당 스터디에 가입되어 있는지 확인
     *
     * @param memberId 회원의 ID
     * @param studyId 스터디의 ID
     * @return 가입되어 있으면 true, 아니면 false
     */
    public boolean isUserMemberOfStudy(Long memberId, Long studyId) {
        return studyMemberRepository.findByMemberIdAndStudyId(memberId, studyId).isPresent();
    }

    /**
     * 회원 아이디와 스터디 아이디를 받아 해당 회원이 스터디에서 모집 관리 권한(permissionRecruitManage)을 가지고 있는지 확인
     *
     * @param memberId 회원의 ID
     * @param studyId 스터디의 ID
     * @return 모집 관리 권한이 true이면 true, 아니면 false
     * @throws IllegalArgumentException 해당 회원이 스터디에 가입되어 있지 않은 경우
     */
    public boolean hasRecruitManagePermission(Long memberId, Long studyId) {
        StudyMember studyMember = getStudyMemberByMemberIdAndStudyId(memberId, studyId);
        return Boolean.TRUE.equals(studyMember.getPermissionRecruitManage());
    }

    /**
     * 회원 아이디와 스터디 아이디를 받아 해당 회원이 스터디에서 게시글 관리 권한(permissionArticleManage)을 가지고 있는지 확인
     *
     * @param memberId 회원의 ID
     * @param studyId 스터디의 ID
     * @return 게시글 관리 권한이 true이면 true, 아니면 false
     * @throws IllegalArgumentException 해당 회원이 스터디에 가입되어 있지 않은 경우
     */
    public boolean hasArticleManagePermission(Long memberId, Long studyId) {
        StudyMember studyMember = getStudyMemberByMemberIdAndStudyId(memberId, studyId);
        return Boolean.TRUE.equals(studyMember.getPermissionArticleManage());
    }

    /**
     * 회원 아이디와 스터디 아이디를 받아 해당 회원이 스터디에서 캘린더 관리 권한(permissionCalendarManage)을 가지고 있는지 확인
     *
     * @param memberId 회원의 ID
     * @param studyId 스터디의 ID
     * @return 캘린더 관리 권한이 true이면 true, 아니면 false
     * @throws IllegalArgumentException 해당 회원이 스터디에 가입되어 있지 않은 경우
     */
    public boolean hasCalendarManagePermission(Long memberId, Long studyId) {
        StudyMember studyMember = getStudyMemberByMemberIdAndStudyId(memberId, studyId);
        return Boolean.TRUE.equals(studyMember.getPermissionCalendarManage());
    }

    /**
     * 회원 아이디와 스터디 아이디를 받아 해당 회원이 스터디에서 설정 관리 권한(permissionSettingManage)을 가지고 있는지 확인
     *
     * @param memberId 회원의 ID
     * @param studyId 스터디의 ID
     * @return 설정 관리 권한이 true이면 true, 아니면 false
     * @throws IllegalArgumentException 해당 회원이 스터디에 가입되어 있지 않은 경우
     */
    public boolean hasSettingManagePermission(Long memberId, Long studyId) {
        StudyMember studyMember = getStudyMemberByMemberIdAndStudyId(memberId, studyId);
        return Boolean.TRUE.equals(studyMember.getPermissionSettingManage());
    }

    /**
     * 회원 아이디와 스터디 아이디를 받아 해당 StudyMember 엔티티를 조회
     *
     * @param memberId 회원의 ID
     * @param studyId 스터디의 ID
     * @return 조회된 StudyMember 엔티티
     * @throws IllegalArgumentException 해당 회원이 스터디에 가입되어 있지 않은 경우
     */
    private StudyMember getStudyMemberByMemberIdAndStudyId(Long memberId, Long studyId) {
        return studyMemberRepository.findByMemberIdAndStudyId(memberId, studyId)
                .orElseThrow(() -> new IllegalArgumentException("Member with id " + memberId +
                        " is not registered in study with id " + studyId));
    }

    /**
     * 스터디에 새로운 멤버를 추가하는 메서드.
     *
     * @param studyId                   가입할 스터디의 식별자
     * @param memberId                  가입할 멤버의 식별자
     * @param position                  스터디 내에서의 포지션
     * @param permissionRecruitManage   모집글 관리 권한 여부
     * @param permissionArticleManage   게시글 관리 권한 여부
     * @param permissionCalendarManage  캘린더 관리 권한 여부
     * @param permissionSettingManage   스터디 설정 관리 권한 여부
     * @return 생성된 StudyMember 엔티티
     */
    public StudyMember addMemberToStudy(Long studyId,
                                        Long memberId,
                                        StudyPosition position,
                                        boolean permissionRecruitManage,
                                        boolean permissionArticleManage,
                                        boolean permissionCalendarManage,
                                        boolean permissionSettingManage) {
        // getReferenceById를 사용하여 실제 데이터를 조회하지 않고 proxy 객체만 획득
        Member member = memberRepository.getReferenceById(memberId);
        Study study = studyRepository.getReferenceById(studyId);

        // StudyMember 엔티티 생성
        StudyMember studyMember = StudyMember.builder()
                .member(member)
                .study(study)
                .position(position)
                .permissionRecruitManage(permissionRecruitManage)
                .permissionArticleManage(permissionArticleManage)
                .permissionCalendarManage(permissionCalendarManage)
                .permissionSettingManage(permissionSettingManage)
                .build();

        emitters.notiAddStudyMember(member.getId(),study.getId());
        // 새 StudyMember 저장
        return studyMemberRepository.save(studyMember);
    }

    /**
     * 스터디에 새로운 멤버를 추가하는 메서드.
     *
     * @param studyTitle                   가입할 스터디의 식별자
     * @param nickname                가입할 멤버의 식별자
     * @param position                  스터디 내에서의 포지션
     * @param permissionRecruitManage   모집글 관리 권한 여부
     * @param permissionArticleManage   게시글 관리 권한 여부
     * @param permissionCalendarManage  캘린더 관리 권한 여부
     * @param permissionSettingManage   스터디 설정 관리 권한 여부
     * @return 생성된 StudyMember 엔티티
     */
    public StudyMember addMemberToStudy(String studyTitle,
                                        String nickname,
                                        StudyPosition position,
                                        boolean permissionRecruitManage,
                                        boolean permissionArticleManage,
                                        boolean permissionCalendarManage,
                                        boolean permissionSettingManage) {
        // getReferenceById를 사용하여 실제 데이터를 조회하지 않고 proxy 객체만 획득
        Member member = memberRepository.findByNickname(nickname).get();
        Study study = studyRepository.findByTitle(studyTitle).get();

        // StudyMember 엔티티 생성
        StudyMember studyMember = StudyMember.builder()
                .member(member)
                .study(study)
                .position(position)
                .permissionRecruitManage(permissionRecruitManage)
                .permissionArticleManage(permissionArticleManage)
                .permissionCalendarManage(permissionCalendarManage)
                .permissionSettingManage(permissionSettingManage)
                .build();

        emitters.notiAddStudyMember(member.getId(),study.getId());
        // 새 StudyMember 저장
        return studyMemberRepository.save(studyMember);
    }

    public void rejectMemberToStudy(String studyTitle,String nickname){
        Member member = memberRepository.findByNickname(nickname).get();
        Study study = studyRepository.findByTitle(studyTitle).get();

        emitters.notiRejectStudyMember(member,study);

    }
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
    /**
     * 멤버 아이디로 해당 멤버가 속한 Study와 position 목록 조회
     * @param memberId 멤버의 ID
     * @return Study와 position을 담은 DTO 리스트
     */
    public List<StudyWithPositionSimpleDto> getStudiesWithPositionByMemberId(Long memberId) {
        return studyMemberRepository.findStudiesWithPositionSimpleByMemberId(memberId);
    }
}
