package com.meossamos.smore.domain.chat.livekit.service;

import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import com.meossamos.smore.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LiveKitService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public void getUserInfo(String token) {
        Authentication authentication = tokenProvider.getAuthentication(token);
//        System.out.println(authentication.toString());
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Long userID = Long.valueOf(Integer.parseInt(principal.getUsername()));

        Member member = memberRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("해당 ID의 회원을 찾을 수 없습니다." + userID));
        System.out.println(member.toString());

        //스터디 이름, 멤버

    }
}
