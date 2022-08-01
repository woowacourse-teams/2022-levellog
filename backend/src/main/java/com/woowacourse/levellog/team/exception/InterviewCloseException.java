package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class InterviewCloseException extends LevellogException {

    public InterviewCloseException(final String message, final String clientMessage) {
        super(clientMessage + message, clientMessage, HttpStatus.BAD_REQUEST);
    }
}
