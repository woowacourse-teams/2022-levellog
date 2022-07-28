package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class HostUnauthorizedException extends LevellogException {

    public HostUnauthorizedException(final String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
