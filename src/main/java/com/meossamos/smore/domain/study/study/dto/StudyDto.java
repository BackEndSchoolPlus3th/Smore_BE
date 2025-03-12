package com.meossamos.smore.domain.study.study.dto;

import com.meossamos.smore.domain.member.member.entity.Member;
import lombok.*;

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
    private Member leader;

    public StudyDto(Long id, String title, String introduction, String hashTags) {
        this.id = id;
        this.title = title;
        this.introduction = introduction;
        this.hashTags = hashTags;
    }
}
