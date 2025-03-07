package com.meossamos.smore.domain.member.member.service;

import com.meossamos.smore.domain.member.member.dto.*;
import com.meossamos.smore.domain.member.member.entity.Authority;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import com.meossamos.smore.global.jwt.TokenProvider;
import com.meossamos.smore.global.sse.SseEmitters;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final SseEmitters sseEmitters;
    public Member saveInitMember(String email, String password, String nickname, @Nullable LocalDate birthdate, @Nullable String region, @Nullable String profileImageUrl) {

        Member member = Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .birthdate(birthdate)
                .region(region)
                .profileImageUrl(profileImageUrl)
                .authority(Authority.ROLE_USER)
                .build();
        return memberRepository.save(member);
    }
    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        //입력한 비밀번호 암호화

        Member member = memberRequestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Transactional
    public LoginResponseDto login(LoginDto loginDto) {
        // 로그인 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        Long memberId = memberRepository.findByEmail(loginDto.getEmail()).get().getId();
        TokenDto tokenDto = tokenProvider.generateTokenDto(memberId,authentication);
        SseEmitter emitter = new SseEmitter();

        // 생성된 emitter를 컬렉션에 추가하여 관리
        sseEmitters.add(tokenDto.getAccessToken(),emitter);

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 인증된 id로 회원 정보 조회
        Member member = memberRepository.findById(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        // LoginResponseDto 생성 (재사용 가능한 형태)
        return LoginResponseDto.builder()
                .token(tokenDto)
                .nickname(member.getNickname())
                .hashTags(member.getHashTags())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }


    @Transactional
    public TokenDto refresh(HttpServletRequest request){

        String requestToken = extractRefreshTokenFromCookies(request);
         Long memberId=tokenProvider.parseClaims(requestToken).get("memberId", Long.class);

        Authentication authentication = tokenProvider.getAuthentication(requestToken);

        return tokenProvider.generateTokenDto(memberId,authentication);
    }
    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("refreshToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    public Long getMaxMemberId() {
        Long maxId = memberRepository.findMaxMemberId();
        return maxId != null ? maxId : 0L;
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new RuntimeException("해당하는 회원이 없습니다."));
    }

    public List<Member> findByIds(List<Long> memberIds) {
        return memberRepository.findByIdIn(memberIds);
    }

    public Member getReferenceById(Long memberId) {
        return memberRepository.getReferenceById(memberId);
    }

    // 이메일 중복 체크
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}
