package com.woowacourse.levellog.interviewquestion.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class InterviewQuestionLikeNotFoundException extends LevellogException {
    private static final String CLIENT_MESSAGE = "인터뷰 질문을 '좋아요'하지 않았습니다.";

    public InterviewQuestionLikeNotFoundException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.NOT_FOUND);
    }
}
