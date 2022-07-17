package com.woowacourse.levellog.authentication.exception;

import com.woowacourse.levellog.exception.LevellogException;
import org.springframework.http.HttpStatus;

/**
 * 존재하지 않는 멤버를 조회하는 경우 발생하는 예외입니다.
 */
public class MemberNotFoundException extends LevellogException {

    private static final String ERROR_MESSAGE = "멤버가 존재하지 않습니다.";

    public MemberNotFoundException() {
        super(ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    public MemberNotFoundException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
