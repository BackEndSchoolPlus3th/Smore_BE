package com.meossamos.smore.domain.study.studyMember.repository;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.study.study.entity.Study;
import com.meossamos.smore.domain.study.studyMember.dto.StudyWithPositionDto;
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

    // 멤버 아이디로 Study, Position, 그리고 CreateDate 함께 조회
    @Query("select new com.meossamos.smore.domain.study.studyMember.dto.StudyWithPositionDto(sm.study, sm.position, sm.createdDate) " +
            "from StudyMember sm where sm.member.id = :memberId")
    List<StudyWithPositionDto> findStudiesWithPositionByMemberId(@Param("memberId") Long memberId);

}

