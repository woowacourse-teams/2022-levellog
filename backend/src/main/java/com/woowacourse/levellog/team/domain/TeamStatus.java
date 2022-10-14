package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.team.exception.TeamAlreadyClosedException;
import com.woowacourse.levellog.team.exception.TeamNotInProgressException;
import com.woowacourse.levellog.team.exception.TeamNotReadyException;
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

    public void validateInProgress() {
        if (this == CLOSED) {
            throw new TeamAlreadyClosedException(DebugMessage.init());
        }

        if (this != IN_PROGRESS) {
            throw new TeamNotInProgressException(DebugMessage.init());
        }
    }

    public void validateReady() {
        if (this != TeamStatus.READY) {
            throw new TeamNotReadyException(DebugMessage.init());
        }
    }
}
