package com.woowacourse.levellog.admin.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class WrongPasswordException extends LevellogException {

    private static final String CLIENT_MESSAGE = "비밀번호가 틀렸습니다.";

    public WrongPasswordException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
