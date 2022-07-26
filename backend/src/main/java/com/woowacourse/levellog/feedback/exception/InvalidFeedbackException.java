package com.woowacourse.levellog.feedback.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class InvalidFeedbackException extends LevellogException {

    public InvalidFeedbackException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
