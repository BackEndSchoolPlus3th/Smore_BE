package com.meossamos.smore.domain.member.member.controller;

import com.meossamos.smore.domain.member.member.dto.*;
import com.meossamos.smore.domain.member.member.dto.update.*;
import com.meossamos.smore.domain.member.member.entity.Member;
import com.meossamos.smore.domain.member.member.service.MemberService;
import com.meossamos.smore.global.jwt.TokenProvider;
import com.meossamos.smore.global.s3.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final S3Service s3Service;

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
                .hashTags(null)
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

    /**
     * 마이페이지 회원 정보 수정용 조회
     * @param userDetails
     * @return
     */
    @GetMapping("/settings")
    public ResponseEntity<MemberSettingResponse> getMemberSettings(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Long memberId = Long.valueOf(userDetails.getUsername());
        Member member = memberService.getReferenceById(memberId);

        List<String> hashTagList = (member.getHashTags() != null && !member.getHashTags().isEmpty())
                ? Arrays.asList(member.getHashTags().split(","))
                : Collections.emptyList();

        MemberSettingResponse response = new MemberSettingResponse(
                member.getEmail(),
                member.getNickname(),
                member.getProfileImageUrl(),
                member.getDescription(),
                member.getBirthdate(),
                member.getRegion(),
                hashTagList
        );

        return ResponseEntity.ok(response);
    }

    /**
     * 회원 정보 수정
     * @param userDetails
     * @param request
     * @return
     */
    @PutMapping("/nickname")
    public ResponseEntity<?> updateNickname(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateNicknameRequest request) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        memberService.updateNickname(memberId, request.getNickname());
        return ResponseEntity.ok(Map.of("message", "Nickname updated"));
    }

    @PutMapping("/description")
    public ResponseEntity<?> updateDescription(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateDescriptionRequest request) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        memberService.updateDescription(memberId, request.getDescription());
        return ResponseEntity.ok(Map.of("message", "Description updated"));
    }

    @PutMapping("/profile-image")
    public ResponseEntity<?> updateProfileImage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateProfileImageUrlRequest request) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        memberService.updateProfileImageUrl(memberId, request.getProfileImageUrl());
        return ResponseEntity.ok(Map.of("message", "Profile image updated"));
    }

    @DeleteMapping("/profile-image")
    public ResponseEntity<?> deleteProfileImage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "profileImageUrl") String profileImageUrl)
    {
        s3Service.deleteFile("profile", profileImageUrl);
        return ResponseEntity.ok(Map.of("message", "Profile image deleted"));
    }

    @PutMapping("/birthdate")
    public ResponseEntity<?> updateBirthdate(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateBirthdateRequest request) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        memberService.updateBirthdate(memberId, LocalDate.parse(request.getBirthdate()));
        return ResponseEntity.ok(Map.of("message", "Birthdate updated"));
    }

    @PutMapping("/region")
    public ResponseEntity<?> updateRegion(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateRegionRequest request) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        memberService.updateRegion(memberId, request.getRegion());
        return ResponseEntity.ok(Map.of("message", "Region updated"));
    }

    @PutMapping("/hashtags")
    public ResponseEntity<?> updateHashTags(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateHashTagsRequest request) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        memberService.updateHashTags(memberId, String.join(",", request.getHashTags()));
        return ResponseEntity.ok(Map.of("message", "HashTags updated"));
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdatePasswordRequest request) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        memberService.updatePassword(memberId, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Password updated"));
    }

    @GetMapping("/bio")
    public ResponseEntity<?> getBio(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Long memberId = Long.valueOf(userDetails.getUsername());
        String bio = memberService.getBio(memberId);
        return ResponseEntity.ok(Map.of("bio", bio));
    }

    @PutMapping("/bio")
    public ResponseEntity<?> updateBio(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateBioRequest request) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Long memberId = Long.valueOf(userDetails.getUsername());
        memberService.updateBio(memberId, request.getBio());
        return ResponseEntity.ok(Map.of("message", "Bio updated"));
    }
}
