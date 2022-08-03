package com.woowacourse.levellog.prequestion.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class InvalidPreQuestionException extends LevellogException {

    public InvalidPreQuestionException(final String exceptionInfo, final String clientMessage) {
        super(clientMessage + exceptionInfo, clientMessage, HttpStatus.BAD_REQUEST);
    }
}
