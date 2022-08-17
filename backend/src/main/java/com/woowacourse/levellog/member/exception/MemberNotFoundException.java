package com.woowacourse.levellog.member.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

/**
 * 존재하지 않는 멤버를 조회하는 경우 발생하는 예외입니다.
 */
public class MemberNotFoundException extends LevellogException {

    private static final String CLIENT_MESSAGE = "멤버가 존재하지 않습니다.";

    public MemberNotFoundException(final String message) {
        super(message, CLIENT_MESSAGE, HttpStatus.NOT_FOUND);
    }
}
