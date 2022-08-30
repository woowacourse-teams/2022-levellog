package com.woowacourse.levellog.member.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class MemberNotAuthorException extends LevellogException {

    private static final String CLIENT_MESSAGE = "작성자가 아닙니다.";

    public MemberNotAuthorException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.UNAUTHORIZED);
    }
}
