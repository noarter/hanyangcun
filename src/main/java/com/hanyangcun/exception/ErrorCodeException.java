package com.hanyangcun.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorCodeException extends Exception {
    private Integer code;
    private String msg;

    public ErrorCodeException() {
        super();
    }

    public ErrorCodeException(String message) {
        super(message);
    }

    public ErrorCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorCodeException(Throwable cause) {
        super(cause);
    }

    protected ErrorCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
