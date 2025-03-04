package com.meossamos.smore.domain.member.member.controller;

import com.meossamos.smore.domain.member.member.dto.*;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.global.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        TokenDto tokenDto = memberService.login(loginDto);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken",tokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)  // 7일 유지
                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .body(tokenDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(memberService.signup(memberRequestDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody AccessTokenDto accessTokenDto,HttpServletRequest request) {
        TokenDto tokenDto = memberService.refresh(request);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(TokenProvider.REFRESH_TOKEN_EXPIRE_TIME)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .body(Map.of("server", "refresh ok"));
    }


    @PostMapping("/check")
    public String check(){
        return "액세스 토큰을 보냈을시 인가 기능이 되는지 확인";
    }
}
