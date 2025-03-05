package com.meossamos.smore.domain.member.member.controller;

import com.meossamos.smore.domain.member.member.dto.*;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.global.jwt.TokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static com.meossamos.smore.global.jwt.JwtFilter.AUTHORIZATION_HEADER;

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
