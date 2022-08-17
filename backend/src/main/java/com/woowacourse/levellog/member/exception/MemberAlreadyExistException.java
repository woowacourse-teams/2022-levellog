package com.woowacourse.levellog.member.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class MemberAlreadyExistException extends LevellogException {

    private static final String CLIENT_MESSAGE = "멤버가 이미 존재합니다.";

    public MemberAlreadyExistException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
