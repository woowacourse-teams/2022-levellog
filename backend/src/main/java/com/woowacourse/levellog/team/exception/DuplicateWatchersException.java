package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class DuplicateWatchersException extends LevellogException {

    private static final String CLIENT_MESSAGE = "중복되는 참관자가 존재합니다.";

    public DuplicateWatchersException(final String message) {
        super(message, CLIENT_MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
