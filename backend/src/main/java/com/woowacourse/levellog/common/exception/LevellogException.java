package com.woowacourse.levellog.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class LevellogException extends RuntimeException {

    private final HttpStatus httpStatus;

    protected LevellogException(final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
