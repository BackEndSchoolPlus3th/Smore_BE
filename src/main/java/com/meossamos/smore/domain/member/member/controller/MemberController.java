package com.meossamos.smore.domain.member.member.controller;

import com.meossamos.smore.domain.member.member.dto.*;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.global.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        LoginResponseDto responseDto = memberService.login(loginDto);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken",responseDto.getToken().getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)  // 7일 유지
                .sameSite("None")
                .build();

        LoginResponseBodyDto loginResponseBodyDto = LoginResponseBodyDto.builder()
                .nickname(responseDto.getNickname())
                .hashTags(responseDto.getHashTags())
                .profileImageUrl(responseDto.getProfileImageUrl())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + responseDto.getToken().getAccessToken())
                .body(loginResponseBodyDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody MemberRequestDto memberRequestDto) {
        // 회원가입 전에 이메일 중복 체크
        if (memberService.existsByEmail(memberRequestDto.getEmail())) {
            // 이미 존재하는 이메일 409 error
            return ResponseEntity.status(409).build();
        }
        MemberResponseDto memberResponseDto = memberService.signup(memberRequestDto);
        return ResponseEntity.ok(memberResponseDto);
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // refreshToken 쿠키 삭제 (maxAge=0)
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(Map.of("server", "logout ok"));
    }



    @PostMapping("/check")
    public String check(){
        return "액세스 토큰을 보냈을시 인가 기능이 되는지 확인";
    }
}
