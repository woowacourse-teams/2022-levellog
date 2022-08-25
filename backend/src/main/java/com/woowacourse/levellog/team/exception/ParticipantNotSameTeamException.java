package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.springframework.http.HttpStatus;

public class ParticipantNotSameTeamException extends LevellogException {

    private static final String CLIENT_MESSAGE = "같은 팀에 속해있지 않습니다.";

    public ParticipantNotSameTeamException(final DebugMessage debugMessage) {
        super(CLIENT_MESSAGE, debugMessage, HttpStatus.UNAUTHORIZED);
    }
}
