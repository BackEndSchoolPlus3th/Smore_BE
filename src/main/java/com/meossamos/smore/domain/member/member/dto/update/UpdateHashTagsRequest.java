package com.meossamos.smore.domain.member.member.dto.update;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateHashTagsRequest {
    private List<String> hashTags;
}
