package com.meossamos.smore.domain.study.study.repository;

import com.meossamos.smore.domain.study.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    Optional<Study> findById(Long id);
    @Query("select s from Study s left join fetch s.groupChatRoom where s.id in :studyIds")
    List<Study> findStudiesWithGroupChatRoom(@Param("studyIds") List<Long> studyIds);
    List<Study> findByIdIn(List<Long> ids);

    Optional<Study> findByTitle(String title);

    @Query("SELECT s FROM Study s " +
            "JOIN FETCH s.studyMemberList sm " +
            "WHERE s.id = :studyId AND sm.member.id = :memberId")
    Optional<Study> findByIdWithMembers(@Param("studyId") Long studyId, @Param("memberId") Long memberId);

}


