package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class TeamNotInProgressException extends LevellogException {

    private static final String CLIENT_MESSAGE = "인터뷰 진행중인 상태가 아닙니다.";

    public TeamNotInProgressException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
