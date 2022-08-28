package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class HostUnauthorizedException extends LevellogException {

    private static final String CLIENT_MESSAGE = "호스트 권한이 없습니다.";

    public HostUnauthorizedException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.UNAUTHORIZED);
    }
}
