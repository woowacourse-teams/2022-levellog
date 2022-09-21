package com.woowacourse.levellog.interviewquestion.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class InterviewQuestionLikesAlreadyExistException extends LevellogException {

    private static final String CLIENT_MESSAGE = "인터뷰 질문에 대한 좋아요가 이미 존재합니다.";

    public InterviewQuestionLikesAlreadyExistException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
