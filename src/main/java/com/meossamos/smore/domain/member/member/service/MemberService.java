package com.meossamos.smore.domain.member.member.service;

import com.meossamos.smore.domain.member.member.dto.*;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.repository.MemberRepository;
import com.meossamos.smore.global.jwt.TokenProvider;
import com.meossamos.smore.global.rsData.RsData;
import com.meossamos.smore.global.sse.SseEmitters;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final SseEmitters sseEmitters;
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
        UsernamePasswordAuthenticationToken authenticationToken = loginDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        SseEmitter emitter = new SseEmitter();

        // 생성된 emitter를 컬렉션에 추가하여 관리
        sseEmitters.add(emitter);

        try {
            // 연결된 클라이언트에게 초기 연결 성공 메시지 전송
            emitter.send(SseEmitter.event()
                    .name("connect")    // 이벤트 이름을 "connect"로 지정
                    .data("connected!")); // 전송할 데이터
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }

        return tokenDto;

    }

    @Transactional
    public TokenDto refresh(HttpServletRequest request){

        String requestToken = extractRefreshTokenFromCookies(request);

        Authentication authentication = tokenProvider.getAuthentication(requestToken);

        return tokenProvider.generateTokenDto(authentication);
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
}
