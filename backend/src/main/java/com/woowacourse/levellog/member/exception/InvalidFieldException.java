package com.woowacourse.levellog.member.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class InvalidFieldException extends LevellogException {

    public InvalidFieldException(final String message, final HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
