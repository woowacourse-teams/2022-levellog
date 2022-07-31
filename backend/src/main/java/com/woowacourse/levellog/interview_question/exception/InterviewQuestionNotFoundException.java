package com.woowacourse.levellog.interview_question.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class InterviewQuestionNotFoundException extends LevellogException {

    private static final String CLIENT_MESSAGE = "인터뷰 질문이 존재하지 않습니다.";

    public InterviewQuestionNotFoundException(final String message) {
        super(message, CLIENT_MESSAGE, HttpStatus.NOT_FOUND);
    }
}
