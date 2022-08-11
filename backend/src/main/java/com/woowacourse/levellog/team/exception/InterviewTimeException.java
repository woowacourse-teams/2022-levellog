package com.woowacourse.levellog.team.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

/**
 * 요청이 들어온 시간과 인터뷰 시작 시간을 비교했을 때 정책과 맞지 않을 경우 발생하는 예외
 */
public class InterviewTimeException extends LevellogException {

    public InterviewTimeException(final String message) {
        super(message, message, HttpStatus.BAD_REQUEST);
    }

    public InterviewTimeException(final String message, final Long teamId, final LocalDateTime startAt,
                                  final LocalDateTime presentTime) {
        super(message + " [teamId : " + teamId + ", startAt : " + startAt + ", presentTime : " + presentTime + "]",
                message, HttpStatus.BAD_REQUEST);
    }
}
