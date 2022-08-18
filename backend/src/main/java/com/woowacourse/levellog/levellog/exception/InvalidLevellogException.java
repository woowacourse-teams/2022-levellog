package com.woowacourse.levellog.levellog.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class InvalidLevellogException extends LevellogException {

    public InvalidLevellogException(final String clientMessage, final String exceptionInfoForDev) {
        super(clientMessage + exceptionInfoForDev, clientMessage, HttpStatus.BAD_REQUEST);
    }
}
