package com.woowacourse.levellog.authentication.exception;

/**
 * 토큰이 유효하지 않거나 Payload에 잘못된 값이 들어있을 경우 발생하는 예외입니다.
 */
public class InvalidTokenException extends RuntimeException {

    private static final String ERROR_MEESAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidTokenException(final Throwable cause) {
        super(ERROR_MEESAGE, cause);
    }
}
