package com.woowacourse.levellog.member.dto.request;

import com.woowacourse.levellog.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class MemberCreateRequest {

    private String nickname;
    private Integer githubId;
    private String profileUrl;

    public Member toEntity() {
        return new Member(nickname, githubId, profileUrl);
    }
}
