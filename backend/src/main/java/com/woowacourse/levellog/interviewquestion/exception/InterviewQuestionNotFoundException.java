package com.woowacourse.levellog.interviewquestion.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

/**
 * 존재하지 않는 인터뷰 질문을 조회하는 경우 발생하는 예외입니다.
 */
public class InterviewQuestionNotFoundException extends LevellogException {

    private static final String CLIENT_MESSAGE = "인터뷰 질문이 존재하지 않습니다.";

    public InterviewQuestionNotFoundException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.NOT_FOUND);
    }
}
