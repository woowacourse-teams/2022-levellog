package com.woowacourse.levellog.authentication.exception;

import com.woowacourse.levellog.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class InvalidFieldException extends LevellogException {

    public InvalidFieldException(final String message, final HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
