package com.woowacourse.levellog.feedback.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class FeedbackNotFoundException extends LevellogException {

    private static final String ERROR_MESSAGE = "피드백이 존재하지 않습니다.";

    public FeedbackNotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public FeedbackNotFoundException() {
        super(ERROR_MESSAGE, HttpStatus.NOT_FOUND);
    }
}
