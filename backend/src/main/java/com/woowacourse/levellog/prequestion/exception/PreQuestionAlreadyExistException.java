package com.woowacourse.levellog.prequestion.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class PreQuestionAlreadyExistException extends LevellogException {

    private static final String CLIENT_MESSAGE = "사전 질문이 이미 존재합니다.";

    public PreQuestionAlreadyExistException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
