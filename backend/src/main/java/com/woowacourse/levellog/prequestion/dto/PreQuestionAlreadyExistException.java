package com.woowacourse.levellog.prequestion.dto;

import com.woowacourse.levellog.common.exception.LevellogException;
import org.springframework.http.HttpStatus;

public class PreQuestionAlreadyExistException extends LevellogException {

    private static final String CLIENT_MESSAGE = "레벨로그의 사전 질문을 이미 작성했습니다.";

    public PreQuestionAlreadyExistException(final String message) {
        super(message, CLIENT_MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
