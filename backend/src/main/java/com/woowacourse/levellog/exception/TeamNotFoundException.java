package com.woowacourse.levellog.exception;

public class TeamNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "팀이 존재하지 않습니다.";

    public TeamNotFoundException(final String message) {
        super(message);
    }

    public TeamNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
