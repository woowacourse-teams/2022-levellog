package com.woowacourse.levellog.exception;

import org.springframework.http.HttpStatus;

public class LevellogAlreadyExistException extends LevellogException {

    private static final String ERROR_MESSAGE = "팀에 레벨로그를 이미 작성했습니다.";

    public LevellogAlreadyExistException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public LevellogAlreadyExistException() {
        super(ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
