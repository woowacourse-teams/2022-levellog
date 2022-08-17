package com.woowacourse.levellog.feedback.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class FeedbackNotFoundException extends LevellogException {

    private static final String CLIENT_MESSAGE = "피드백이 존재하지 않습니다.";

    public FeedbackNotFoundException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.NOT_FOUND);
    }
}
