package com.woowacourse.levellog.feedback.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class InvalidFeedbackException extends LevellogException {

    public InvalidFeedbackException(final String clientMessage, final String exceptionInfoForDev) {
        super(clientMessage + exceptionInfoForDev, clientMessage, HttpStatus.BAD_REQUEST);
    }
}
