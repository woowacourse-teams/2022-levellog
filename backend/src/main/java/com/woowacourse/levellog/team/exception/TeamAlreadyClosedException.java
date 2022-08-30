package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class TeamAlreadyClosedException extends LevellogException {

    private static final String CLIENT_MESSAGE = "이미 인터뷰가 종료된 팀입니다.";

    public TeamAlreadyClosedException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
