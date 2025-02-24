package com.meossamos.smore.domain.study.studyMember.repository;

import com.meossamos.smore.domain.study.studyMember.entity.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
}
