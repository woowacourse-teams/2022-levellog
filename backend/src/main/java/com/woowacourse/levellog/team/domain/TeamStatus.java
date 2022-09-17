package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import java.time.LocalDateTime;
import java.util.Arrays;
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

    public static boolean checkClosed(final String status) {
        if (ALL.equals(status)) {
            return true;
        }

        return Arrays.stream(values())
                .filter(it -> it.getStatus().equals(status))
                .findFirst()
                .map(TeamStatus::isClosed)
                .orElseThrow(() -> new InvalidFieldException("입력 받은 status가 올바르지 않습니다.", DebugMessage.init()
                        .append("status", status)));
    }

    public static TeamStatus of(final boolean isClosed, final LocalDateTime startAt, final LocalDateTime presentTime) {
        if (isClosed) {
            return TeamStatus.CLOSED;
        }
        if (startAt.isAfter(presentTime)) {
            return TeamStatus.READY;
        }
        return TeamStatus.IN_PROGRESS;
    }

    public boolean isSame(final String status) {
        return this.status.equals(status);
    }
}
