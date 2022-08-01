package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class HostUnauthorizedException extends LevellogException {

    private static final String CLIENT_MESSAGE = "호스트 권한이 없습니다.";

    public HostUnauthorizedException(final String message) {
        super(message, CLIENT_MESSAGE, HttpStatus.UNAUTHORIZED);
    }
}
