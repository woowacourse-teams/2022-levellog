package com.woowacourse.levellog.exception;

public class FeedbackNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "피드백이 존재하지 않습니다.";

    public FeedbackNotFoundException(final String message) {
        super(message);
    }

    public FeedbackNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
