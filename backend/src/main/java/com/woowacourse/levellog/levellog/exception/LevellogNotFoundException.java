package com.woowacourse.levellog.levellog.exception;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class LevellogNotFoundException extends LevellogException {

<<<<<<< HEAD
    private static final String CLIENT_MESSAGE = "레벨로그가 존재하지 않습니다.";

    public LevellogNotFoundException(final String message) {
        super(message, CLIENT_MESSAGE, HttpStatus.NOT_FOUND);
=======
    private static final String ERROR_MESSAGE = "레벨로그가 존재하지 않습니다.";

    public LevellogNotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public LevellogNotFoundException() {
        super(ERROR_MESSAGE, HttpStatus.NOT_FOUND);
>>>>>>> main
    }
}
