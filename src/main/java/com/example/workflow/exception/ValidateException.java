package com.example.workflow.exception;

public class ValidateException extends RuntimeException {
    private int code;
    private String msg;

    public ValidateException() {
        this(1001, "接口错误");
    }

    public ValidateException(String msg) {
        this(1001, msg);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ValidateException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
