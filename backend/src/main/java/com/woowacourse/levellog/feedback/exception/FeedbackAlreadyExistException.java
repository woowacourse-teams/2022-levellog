package com.woowacourse.levellog.feedback.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class FeedbackAlreadyExistException extends LevellogException {

    private static final String CLIENT_MESSAGE = "피드백이 이미 존재합니다.";

    public FeedbackAlreadyExistException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
