package com.woowacourse.levellog.team.domain;


import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamFilterCondition {

    OPEN("open", false),
    CLOSE("close", true);

    private final String status;
    private final boolean isClosed;

    public static TeamFilterCondition from(final String condition) {
        return Arrays.stream(values())
                .filter(it -> it.status.equals(condition))
                .findFirst()
                .orElseThrow(() -> new InvalidFieldException("입력 받은 필터링 조건이 올바르지 않습니다.", DebugMessage.init()
                        .append("status", condition)));
    }
}
