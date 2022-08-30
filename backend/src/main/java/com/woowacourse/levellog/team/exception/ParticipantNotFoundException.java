package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class ParticipantNotFoundException extends LevellogException {

    private static final String CLIENT_MESSAGE = "참가자가 존재하지 않습니다.";

    public ParticipantNotFoundException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.NOT_FOUND);
    }
}
