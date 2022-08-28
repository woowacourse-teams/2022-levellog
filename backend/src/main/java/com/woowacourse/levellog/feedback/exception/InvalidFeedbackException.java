package com.woowacourse.levellog.feedback.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class InvalidFeedbackException extends LevellogException {

    private static final String CLIENT_MESSAGE = "잘못된 피드백 요청입니다.";

    public InvalidFeedbackException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
