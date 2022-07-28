package com.woowacourse.levellog.levellog.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class LevellogAlreadyExistException extends LevellogException {

    private static final String CLIENT_MESSAGE = "팀에 레벨로그를 이미 작성했습니다.";

    public LevellogAlreadyExistException(final String message) {
        super(message, CLIENT_MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
