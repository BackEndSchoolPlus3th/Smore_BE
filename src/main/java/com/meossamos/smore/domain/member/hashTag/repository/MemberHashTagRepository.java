package com.meossamos.smore.domain.member.hashTag.repository;

import com.meossamos.smore.domain.member.hashTag.entity.MemberHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberHashTagRepository extends JpaRepository<MemberHashTag, Long> {
}
