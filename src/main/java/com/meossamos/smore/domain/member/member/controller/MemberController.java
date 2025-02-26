package com.meossamos.smore.domain.member.member.controller;

import com.meossamos.smore.domain.member.member.dto.LoginDto;
import com.meossamos.smore.domain.member.member.dto.MemberRequestDto;
import com.meossamos.smore.domain.member.member.dto.MemberResponseDto;
import com.meossamos.smore.domain.member.member.dto.TokenDto;
import com.meossamos.smore.domain.member.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginDto loginDto, HttpServletResponse response){
        TokenDto tokenDto = memberService.login(loginDto);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken",tokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)  // 7일 유지
                .sameSite("Strict")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        response.setHeader(HttpHeaders.AUTHORIZATION,"Bearer "+ tokenDto.getAccessToken());

        return tokenDto;
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(memberService.signup(memberRequestDto));
    }

    @GetMapping("/check")
    public String check(){
        return "액세스 토큰을 보냈을시 인가 기능이 되는지 확인";
    }
}
