package com.woowacourse.levellog.feedback.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class FeedbackAlreadyExistException extends LevellogException {

    private static final String ERROR_MESSAGE = "이미 피드백이 존재합니다. LevellogId : ";

    public FeedbackAlreadyExistException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public FeedbackAlreadyExistException(final Long levellogId) {
        super(ERROR_MESSAGE + levellogId, HttpStatus.BAD_REQUEST);
    }
}
