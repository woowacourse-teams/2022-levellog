package com.woowacourse.levellog.team.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamStatus {

    READY("ready", false),
    IN_PROGRESS("in-progress", false),
    CLOSED("closed", true);

    private static final String ALL = "all";

    private final String status;
    private final boolean isClosed;

    public static TeamStatus of(final boolean isClosed, final LocalDateTime startAt, final LocalDateTime presentTime) {
        if (isClosed) {
            return TeamStatus.CLOSED;
        }
        if (startAt.isAfter(presentTime)) {
            return TeamStatus.READY;
        }
        return TeamStatus.IN_PROGRESS;
    }
}
