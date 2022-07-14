package com.woowacourse.levellog.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(LevellogException.class)
    public ResponseEntity<ExceptionResponse> handleLevellogException(final LevellogException e) {
        final ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleUnexpectedException(final Exception e) {
        final ExceptionResponse response = new ExceptionResponse(e.getMessage());
        return ResponseEntity
                .internalServerError()
                .body(response);
    }
}
