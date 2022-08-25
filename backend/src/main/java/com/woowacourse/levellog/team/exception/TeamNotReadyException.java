package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class TeamNotReadyException extends LevellogException {

    private static final String CLIENT_MESSAGE = "인터뷰 준비 상태가 아닙니다.";

    public TeamNotReadyException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
