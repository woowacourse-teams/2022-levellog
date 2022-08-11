package com.woowacourse.levellog.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class LevellogException extends RuntimeException {

    private final String clientMessage;
    private final HttpStatus httpStatus;

    protected LevellogException(final String message, final String clientMessage, final HttpStatus httpStatus) {
        super(message);
        this.clientMessage = clientMessage;
        this.httpStatus = httpStatus;
    }
}
