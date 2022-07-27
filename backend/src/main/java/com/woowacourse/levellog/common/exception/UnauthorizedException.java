package com.woowacourse.levellog.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends LevellogException {

    private static final String ERROR_MESSAGE = "권한이 없습니다.";

    public UnauthorizedException(final String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException() {
        super(ERROR_MESSAGE, HttpStatus.UNAUTHORIZED);
    }
}
