package com.woowacourse.levellog.interviewquestion.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class InvalidInterviewQuestionException extends LevellogException {

    public InvalidInterviewQuestionException(final String clientMessage, final DebugMessage debugMessage) {
        super(clientMessage, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
