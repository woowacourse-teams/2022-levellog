package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamStatus {

    READY("ready", false),
    IN_PROGRESS("in-progress", false),
    CLOSED("closed", true);

    private final String status;
    private final boolean isClosed;

    public static boolean isClosed(final String status) {
        return Arrays.stream(values())
                .filter(it -> it.getStatus().equals(status))
                .findFirst()
                .map(TeamStatus::isClosed)
                .orElseThrow(() -> new InvalidFieldException("입력 받은 status가 올바르지 않습니다."));
    }

    public boolean isSame(final String status) {
        return this.status.equals(status);
    }
}
