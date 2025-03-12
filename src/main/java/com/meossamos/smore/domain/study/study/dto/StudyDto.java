package com.meossamos.smore.domain.study.study.dto;

import com.meossamos.smore.domain.member.member.dto.MemberResponseDto;
import com.meossamos.smore.domain.member.member.entity.Member;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudyDto {

    private Long id;
    private String title;
    private String introduction;
    private String hashTags;
    private Integer memberCnt;
    private String imageUrls;
    // 리더 정보는 MemberResponseDto로 전달 (예: 이메일, 닉네임 등 필요한 정보만 포함)
    private MemberResponseDto leader;
    // 가입된 멤버들의 정보를 담을 리스트 (필요한 경우 확장)
    private List<MemberResponseDto> members;

    public StudyDto(Long id, String title, String introduction, String hashTags) {
        this.id = id;
        this.title = title;
        this.introduction = introduction;
        this.hashTags = hashTags;
    }
}
