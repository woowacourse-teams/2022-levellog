package com.woowacourse.levellog.team.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SimpleParticipant {

    private final Long teamId;
    private final Long memberId;
    private final boolean isHost;
    private final boolean isWatcher;

    public boolean isParticipant() {
        return !isWatcher;
    }
}
