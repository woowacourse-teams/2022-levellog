package com.woowacourse.levellog.feedback.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class FeedbackAlreadyExistException extends LevellogException {

    private static final String ERROR_MESSAGE = "이미 피드백이 존재합니다.";

    public FeedbackAlreadyExistException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public FeedbackAlreadyExistException() {
        super(ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
