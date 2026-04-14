package com.example.lab203.common.exception;

import com.example.lab203.common.result.Result;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ????????
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * ???????
     *
     * @param exception ????
     * @return ??????
     */
    @ExceptionHandler(BizException.class)
    public Result<Void> handleBizException(BizException exception) {
        log.warn("[GlobalExceptionHandler] ????: {}", exception.getMessage());
        return Result.fail(exception.getCode(), exception.getMessage());
    }

    /**
     * ?????????
     *
     * @param exception ??????
     * @return ??????
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
    public Result<Void> handleValidationException(Exception exception) {
        log.warn("[GlobalExceptionHandler] ??????: {}", exception.getMessage());
        return Result.fail(400, "???????");
    }

    /**
     * ???????
     *
     * @param exception ????
     * @return ??????
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception exception) {
        log.error("[GlobalExceptionHandler] ????", exception);
        return Result.fail(500, "????????????");
    }
}
