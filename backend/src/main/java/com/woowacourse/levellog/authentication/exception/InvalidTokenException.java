package com.woowacourse.levellog.authentication.exception;

import com.woowacourse.levellog.exception.LevellogException;
import org.springframework.http.HttpStatus;

/**
 * 토큰이 유효하지 않거나 Payload에 잘못된 값이 들어있을 경우 발생하는 예외입니다.
 */
public class InvalidTokenException extends LevellogException {

    private static final String ERROR_MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    public InvalidTokenException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
