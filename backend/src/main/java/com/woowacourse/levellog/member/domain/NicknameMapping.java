package com.woowacourse.levellog.member.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NicknameMapping extends BaseEntity {

    @Column(nullable = false)
    private String githubNickname;

    @Column(nullable = false)
    private String crewNickname;

    public NicknameMapping(final String githubNickname, final String crewNickname) {
        this.githubNickname = githubNickname;
        this.crewNickname = crewNickname;
    }
}
