package com.meossamos.smore.domain.member.member.dto;


import com.meossamos.smore.domain.member.member.entity.Authority;
import com.meossamos.smore.domain.member.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    private String email;
    private String password;
    private String nickname;
    private LocalDate birthdate;
    private String region;
    private String hashTags;
    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .nickname(nickname)
            .birthdate(birthdate)
            .region(region)
            .hashTags(hashTags)
            .authority(Authority.ROLE_USER)
            .build();

    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
