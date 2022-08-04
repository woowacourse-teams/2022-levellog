package com.woowacourse.levellog.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

<<<<<<< HEAD
// TODO: 2022/07/26 clientMessage 필드 추가 ControllerAdvice 수정
// TODO: 2022/07/26 ControllerAdvice 수정
@Getter
public abstract class LevellogException extends RuntimeException {

    private final String clientMessage;
    private final HttpStatus httpStatus;

    // TODO: 2022/07/28 삭제되어야함
    @Deprecated
    protected LevellogException(final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.clientMessage = null;
    }

    protected LevellogException(final String message, final String clientMessage, final HttpStatus httpStatus) {
        super(message);
        this.clientMessage = clientMessage;
        this.httpStatus = httpStatus;
=======
@Getter
public abstract class LevellogException extends RuntimeException {

    private final HttpStatus httpStatus;

    protected LevellogException(final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
>>>>>>> main
    }
}
