package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

/**
 * 요청이 들어온 시간과 인터뷰 시작 시간을 비교했을 때 정책과 맞지 않을 경우 발생하는 예외
 */
public class TeamTimeException extends LevellogException {

    private static final String CLIENT_MESSAGE = "잘못된 인터뷰 시작 시간입니다.";

    public TeamTimeException(final String clientMessage, final String exceptionInfoForDev) {
        super(clientMessage + exceptionInfoForDev, clientMessage, HttpStatus.BAD_REQUEST);
    }

    public TeamTimeException(final String message) {
        super(message, message, HttpStatus.BAD_REQUEST);
    }
}
