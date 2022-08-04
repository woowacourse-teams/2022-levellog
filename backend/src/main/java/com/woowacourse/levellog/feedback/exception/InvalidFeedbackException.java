package com.woowacourse.levellog.feedback.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class InvalidFeedbackException extends LevellogException {

<<<<<<< HEAD
    public InvalidFeedbackException(final String exceptionInfo, final String clientMessage) {
        super(clientMessage + exceptionInfo, clientMessage, HttpStatus.BAD_REQUEST);
=======
    public InvalidFeedbackException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
>>>>>>> main
    }
}
