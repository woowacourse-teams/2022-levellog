package com.woowacourse.levellog.feedback.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class FeedbackNotFoundException extends LevellogException {

    public FeedbackNotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
