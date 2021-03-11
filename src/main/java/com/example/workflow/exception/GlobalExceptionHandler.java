package com.example.workflow.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     *
     */
    @ExceptionHandler(value = DefinitionException.class)
    @ResponseBody
    public Result bizExceptionHandler(DefinitionException e) {
        System.out.println(e);
        return Result.defineError(e);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result APIExceptionHandler(MethodArgumentNotValidException e) {

        return Result.validateError(e);
    }

    /**
     * 处理其他异常
     *
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler( Exception e) {
        System.out.println(e);
        ErrorEnum INTERNAL_SERVER_ERROR = new ErrorEnum(500,"服务错误");
        return Result.otherError(INTERNAL_SERVER_ERROR);
    }
}