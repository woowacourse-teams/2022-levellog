package com.woowacourse.levellog.prequestion.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class InvalidPreQuestionException extends LevellogException {

    private static final String CLIENT_MESSAGE = "잘못된 사전 질문 요청입니다.";

    public InvalidPreQuestionException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
