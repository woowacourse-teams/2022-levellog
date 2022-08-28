package com.woowacourse.levellog.authentication.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

/**
 * 토큰이 유효하지 않거나 Payload에 잘못된 값이 들어있을 경우 발생하는 예외입니다.
 */
public class InvalidTokenException extends LevellogException {

    private static final String CLIENT_MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
