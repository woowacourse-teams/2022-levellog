package com.woowacourse.levellog.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// TODO: 2022/07/26 clientMessage 필드 추가 & ControllerAdvice 수정
@Getter
public abstract class LevellogException extends RuntimeException {

    private final HttpStatus httpStatus;

    protected LevellogException(final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
