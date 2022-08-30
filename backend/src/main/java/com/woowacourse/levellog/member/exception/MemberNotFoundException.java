package com.woowacourse.levellog.member.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

/**
 * 존재하지 않는 멤버를 조회하는 경우 발생하는 예외입니다.
 */
public class MemberNotFoundException extends LevellogException {

    private static final String CLIENT_MESSAGE = "멤버가 존재하지 않습니다.";

    public MemberNotFoundException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.BAD_REQUEST);
    }
}
