package com.woowacourse.levellog.levellog.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class LevellogNotFoundException extends LevellogException {

    private static final String CLIENT_MESSAGE = "레벨로그가 존재하지 않습니다.";

    public LevellogNotFoundException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.NOT_FOUND);
    }
}
