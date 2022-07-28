package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class TeamNotFoundException extends LevellogException {

    public TeamNotFoundException(final String message, final String clientMessage) {
        super(message, clientMessage, HttpStatus.NOT_FOUND);
    }
}
