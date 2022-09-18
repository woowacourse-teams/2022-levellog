package com.woowacourse.levellog.interviewquestion.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class InterviewQuestionAlreadyExistException extends LevellogException {

    private static final String CLIENT_MESSAGE = "인터뷰 질문이 이미 존재합니다.";

    public InterviewQuestionAlreadyExistException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
