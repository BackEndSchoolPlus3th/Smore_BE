package com.meossamos.smore.domain.member.member.repository;

import com.meossamos.smore.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select max(m.id) from Member m")
    Long findMaxMemberId();
}
