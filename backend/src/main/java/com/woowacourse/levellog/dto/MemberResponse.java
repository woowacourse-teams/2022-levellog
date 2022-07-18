package com.woowacourse.levellog.dto;

import com.woowacourse.levellog.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String nickname;
    private String profileUrl;

    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getNickname(), member.getProfileUrl());
    }
}
