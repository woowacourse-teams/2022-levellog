package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class TeamNotFoundException extends LevellogException {

    private static final String ERROR_MESSAGE = "팀이 존재하지 않습니다.";

    public TeamNotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public TeamNotFoundException() {
        super(ERROR_MESSAGE, HttpStatus.NOT_FOUND);
    }
}
