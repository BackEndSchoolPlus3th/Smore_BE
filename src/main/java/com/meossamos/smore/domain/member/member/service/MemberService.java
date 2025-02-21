package com.meossamos.smore.domain.member.member.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }
}
