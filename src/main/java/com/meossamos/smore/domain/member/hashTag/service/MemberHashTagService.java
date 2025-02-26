package com.meossamos.smore.domain.member.hashTag.service;

import com.meossamos.smore.domain.member.hashTag.entity.MemberHashTag;
import com.meossamos.smore.domain.member.hashTag.repository.MemberHashTagRepository;
import com.meossamos.smore.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberHashTagService {
    private final MemberHashTagRepository memberHashTagRepository;

    public MemberHashTag saveMemberHashTag(String hashTag, Member member) {
        MemberHashTag memberHashTag = MemberHashTag.builder()
                .hashTag(hashTag)
                .member(member)
                .build();

        return memberHashTagRepository.save(memberHashTag);
    }
}
