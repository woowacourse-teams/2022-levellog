package com.woowacourse.levellog.exception;

public class LevellogNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "레벨로그가 존재하지 않습니다.";

    public LevellogNotFoundException(final String message) {
        super(message);
    }

    public LevellogNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
