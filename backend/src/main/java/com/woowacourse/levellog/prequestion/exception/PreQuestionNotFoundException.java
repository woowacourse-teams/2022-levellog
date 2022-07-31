package com.woowacourse.levellog.prequestion.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class PreQuestionNotFoundException extends LevellogException {

    public PreQuestionNotFoundException(final String message, final String clientMessage) {
        super(message, clientMessage, HttpStatus.NOT_FOUND);
    }
}
