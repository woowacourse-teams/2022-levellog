package com.woowacourse.levellog.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends LevellogException {

    private static final String CLIENT_MESSAGE = "권한이 없습니다.";

    public UnauthorizedException(final String message) {
        super(message, CLIENT_MESSAGE, HttpStatus.UNAUTHORIZED);
    }
}
