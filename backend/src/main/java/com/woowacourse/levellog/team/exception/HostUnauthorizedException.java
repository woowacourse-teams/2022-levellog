package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class HostUnauthorizedException extends LevellogException {

    private static final String ERROR_MESSAGE = "호스트 권한이 없습니다.";

    public HostUnauthorizedException(final String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public HostUnauthorizedException() {
        super(ERROR_MESSAGE, HttpStatus.UNAUTHORIZED);
    }
}
