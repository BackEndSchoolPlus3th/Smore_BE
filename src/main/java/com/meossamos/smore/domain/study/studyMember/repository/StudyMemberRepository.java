package com.meossamos.smore.domain.study.studyMember.repository;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.studyMember.dto.StudyWithPositionSimpleDto;
import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    List<StudyMember> findByMemberId(Long memberId);
    Optional<StudyMember> findByMemberAndStudy(Member member, Study study);

    // 멤버 아이디로 스터디 리스트 조회
    @Query("select sm.study from StudyMember sm where sm.member.id = :memberId")
    List<Study> findStudiesByMemberId(@Param("memberId") Long memberId);

    // 회원 아이디로 Study의 필요한 정보와 포지션, 가입일자만 조회하는 최적화 쿼리
    @Query("select new com.meossamos.smore.domain.study.studyMember.dto.StudyWithPositionSimpleDto(" +
            "new com.meossamos.smore.domain.study.studyMember.dto.StudySimpleDto(s.id, s.title, s.introduction, s.thumbnailUrl, s.hashTags, s.memberCnt), " +
            "sm.position, sm.createdDate) " +
            "from StudyMember sm join sm.study s where sm.member.id = :memberId")
    List<StudyWithPositionSimpleDto> findStudiesWithPositionSimpleByMemberId(@Param("memberId") Long memberId);


    // memberId와 studyId로 StudyMember 단건 조회
    @Query("select sm from StudyMember sm where sm.member.id = :memberId and sm.study.id = :studyId")
    Optional<StudyMember> findByMemberIdAndStudyId(@Param("memberId") Long memberId, @Param("studyId") Long studyId);

    List<StudyMember> findByMember(Member targetMember);

    List<StudyMember> findByStudyId(Long studyId);
    Optional<StudyMember> findByStudyIdAndMemberId(Long studyId, Long memberId);
}

