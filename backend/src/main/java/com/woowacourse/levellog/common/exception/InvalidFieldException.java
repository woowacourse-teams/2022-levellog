package com.woowacourse.levellog.common.exception;

import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

/**
 * 객체 생성시 매개변수가 유효하지 않은 경우 던지는 예외
 */
public class InvalidFieldException extends LevellogException {

    public InvalidFieldException(final String clientMessage, final DebugMessage debugMessage) {
        super(clientMessage, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
