package com.woowacourse.levellog.common.exception;

import com.woowacourse.levellog.common.support.DebugMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class LevellogException extends RuntimeException {

    private final String clientMessage;
    private final HttpStatus httpStatus;

    protected LevellogException(final String clientMessage, final DebugMessage debugMessage,
                                final HttpStatus httpStatus) {
        super(clientMessage + debugMessage.build());
        this.clientMessage = clientMessage;
        this.httpStatus = httpStatus;
    }
}
