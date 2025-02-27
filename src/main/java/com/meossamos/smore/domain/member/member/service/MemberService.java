package com.meossamos.smore.domain.member.member.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member saveMember(String email, String password, String nickname, @Nullable LocalDate birthdate, @Nullable String region, @Nullable String profileImageUrl) {
        Member member = Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .birthdate(birthdate)
                .region(region)
                .profileImageUrl(profileImageUrl)
                .build();
        return memberRepository.save(member);
    }

    public Long getMaxMemberId() {
        Long maxId = memberRepository.findMaxMemberId();
        return maxId != null ? maxId : 0L;
    }

}
