package com.woowacourse.levellog.member.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class InvalidMemberException extends LevellogException {

    private static final String CLIENT_MESSAGE = "잘못된 멤버 요청입니다.";

    public InvalidMemberException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
