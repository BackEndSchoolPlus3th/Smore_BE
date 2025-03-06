package com.meossamos.smore.domain.member.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private TokenDto token;
    private String nickname;
    private String hashTags;
    private String profileImageUrl;
}
