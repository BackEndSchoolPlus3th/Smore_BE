package com.meossamos.smore.domain.member.member.repository;

import com.meossamos.smore.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select max(m.id) from Member m")
    Long findMaxMemberId();

    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);

    List<Member> findByIdIn(List<Long> memberIds);

    // memberId를 이용해 hashTags 컬럼만 조회 (존재하지 않을 경우는 Optional.empty, 회원은 있지만 hashTags가 null이면 빈 문자열 반환)
    @Query("select coalesce(m.hashTags, '') from Member m where m.id = :memberId")
    Optional<String> findHashTagsByMemberId(@Param("memberId") Long memberId);
}
