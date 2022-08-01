package com.woowacourse.levellog.common.presentation;

import com.woowacourse.levellog.common.dto.ExceptionResponse;
import com.woowacourse.levellog.common.exception.LevellogException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(LevellogException.class)
    public ResponseEntity<ExceptionResponse> handleLevellogException(final LevellogException e) {
        log.info("[{}]{} : {}", getCorrelationId(), e.getClass().getSimpleName(), e.getMessage());
        return toResponseEntity(e.getClientMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(final MethodArgumentNotValidException e) {
        final FieldError fieldError = e.getFieldErrors()
                .get(0);
        final String message = fieldError.getField() + " " + fieldError.getDefaultMessage();
        return toResponseEntity(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleUnexpectedException(final Exception e) {
        log.warn("[{}]", getCorrelationId(), e);
        return toResponseEntity("예상치 못한 예외가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> toResponseEntity(final String message, final HttpStatus httpStatus) {
        final ExceptionResponse response = new ExceptionResponse(message);
        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }

    private String getCorrelationId() {
        return MDC.get("correlationId");
    }
}
