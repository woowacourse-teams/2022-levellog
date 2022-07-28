package com.woowacourse.levellog.feedback.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class FeedbackAlreadyExistException extends LevellogException {

    public FeedbackAlreadyExistException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
