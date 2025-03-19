package com.meossamos.smore.domain.study.studyMember.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParticipantDto {
    private Long memberId;
    private String memberName;
    private String profileImageUrl; 
}
