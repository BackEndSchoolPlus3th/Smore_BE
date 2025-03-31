package com.meossamos.smore.domain.member.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseBodyDto {
    private String nickname;
    private List<String> hashTags;
    private String profileImageUrl;
}
