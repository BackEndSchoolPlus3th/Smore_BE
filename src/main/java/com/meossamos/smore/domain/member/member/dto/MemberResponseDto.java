package com.meossamos.smore.domain.member.member.dto;

import com.meossamos.smore.domain.member.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String email;
    private String nickname;
    private String profileImageUrl;

    public static MemberResponseDto of(Member member) {

        return new MemberResponseDto(
                member.getEmail(),
                member.getNickname(),
                member.getProfileImageUrl()
        );
    }
}
