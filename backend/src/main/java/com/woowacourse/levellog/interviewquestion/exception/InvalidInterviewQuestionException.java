package com.woowacourse.levellog.interviewquestion.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class InvalidInterviewQuestionException extends LevellogException {

    private static final String CLIENT_MESSAGE = "잘못된 인터뷰 질문 요청입니다.";

    public InvalidInterviewQuestionException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
