package com.woowacourse.levellog.feedback.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class FeedbackAlreadyExistException extends LevellogException {

    private static final String CLIENT_MESSAGE = "피드백이 이미 존재합니다.";

    public FeedbackAlreadyExistException(final String message) {
        super(message, CLIENT_MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
