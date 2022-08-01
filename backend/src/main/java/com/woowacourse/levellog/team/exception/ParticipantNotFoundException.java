package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class ParticipantNotFoundException extends LevellogException {

    private static final String CLIENT_MESSAGE = "참가자를 찾을 수 없습니다.";

    public ParticipantNotFoundException(final String message) {
        super(message, CLIENT_MESSAGE, HttpStatus.NOT_FOUND);
    }
}
