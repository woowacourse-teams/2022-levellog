package com.woowacourse.levellog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class LevellogException extends RuntimeException {

    private final HttpStatus httpStatus;

    protected LevellogException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
