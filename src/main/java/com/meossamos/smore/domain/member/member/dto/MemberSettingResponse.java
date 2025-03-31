package com.meossamos.smore.domain.member.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSettingResponse {
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String description;
    private LocalDate birthdate;
    private String region;
    private List<String> hashTags;
}
