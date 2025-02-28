package com.meossamos.smore.domain.study.study.dto;

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
    private List<String> hashTags;
    private Integer memberCnt;
    private String imageUrls;
    private Member leader;
}
