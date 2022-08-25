package com.woowacourse.levellog.prequestion.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class PreQuestionNotFoundException extends LevellogException {

    private static final String CLIENT_MESSAGE = "사전 질문이 존재하지 않습니다.";

    public PreQuestionNotFoundException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.NOT_FOUND);
    }
}
