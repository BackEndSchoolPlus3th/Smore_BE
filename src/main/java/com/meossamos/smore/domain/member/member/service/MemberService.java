package com.meossamos.smore.domain.member.member.service;

import com.meossamos.smore.domain.member.member.dto.LoginDto;
import com.meossamos.smore.domain.member.member.dto.MemberRequestDto;
import com.meossamos.smore.domain.member.member.dto.MemberResponseDto;
import com.meossamos.smore.domain.member.member.dto.TokenDto;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import com.meossamos.smore.global.jwt.TokenProvider;
import com.meossamos.smore.global.rsData.RsData;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    public Member saveMember(String email, String password, String nickname, @Nullable LocalDate birthdate, @Nullable String region, @Nullable String hashTags) {
        Member member = Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .birthdate(birthdate)
                .region(region)
                .hashTags(hashTags)
                .build();
        return memberRepository.save(member);
    }
    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }
        //입력한 비밀번호 암호화

        Member member = memberRequestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }
    @Transactional
    public TokenDto login(LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken =loginDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        return tokenDto;
    }

    public Long getMaxMemberId() {
        Long maxId = memberRepository.findMaxMemberId();
        return maxId != null ? maxId : 0L;
    }

}
