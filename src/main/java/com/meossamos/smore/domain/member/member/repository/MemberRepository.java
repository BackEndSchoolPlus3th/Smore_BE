package com.meossamos.smore.domain.member.member.repository;

import com.meossamos.smore.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select max(m.id) from Member m")
    Long findMaxMemberId();

    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
}
