package com.woowacourse.levellog.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberCreateDto {
    private String nickname;

    private Integer githubId;

    private String profileUrl;
}
