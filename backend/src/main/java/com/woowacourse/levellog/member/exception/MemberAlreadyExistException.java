package com.woowacourse.levellog.member.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class MemberAlreadyExistException extends LevellogException {

    private static final String ERROR_MESSAGE = "멤버가 이미 존재합니다.";

    public MemberAlreadyExistException(final String message) {
        super(message, ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
