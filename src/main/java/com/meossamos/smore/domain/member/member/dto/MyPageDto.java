package com.meossamos.smore.domain.member.member.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageDto {
    String email;
    String nickName;
    LocalDate birthdate;
    String region;
    String hashTags;
    String profileImageUrl;

}
