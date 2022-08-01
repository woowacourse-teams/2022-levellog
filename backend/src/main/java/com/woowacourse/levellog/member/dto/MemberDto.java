package com.woowacourse.levellog.member.dto;

import com.woowacourse.levellog.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode
public class MemberDto {

    private Long id;
    private String nickname;
    private String profileUrl;

    public static MemberDto from(final Member member) {
        return new MemberDto(member.getId(), member.getNickname(), member.getProfileUrl());
    }
}
