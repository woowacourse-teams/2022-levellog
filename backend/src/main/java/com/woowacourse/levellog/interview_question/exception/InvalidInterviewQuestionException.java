package com.woowacourse.levellog.interview_question.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class InvalidInterviewQuestionException extends LevellogException {

    public InvalidInterviewQuestionException(final String exceptionInfo, final String clientMessage) {
        super(clientMessage + exceptionInfo, clientMessage, HttpStatus.BAD_REQUEST);
    }
}
