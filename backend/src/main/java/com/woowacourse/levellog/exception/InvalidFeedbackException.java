package com.woowacourse.levellog.exception;

import org.springframework.http.HttpStatus;

public class InvalidFeedbackException extends LevellogException {

    public InvalidFeedbackException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
