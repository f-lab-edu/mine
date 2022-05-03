package com.mine.common.exception;

import com.mine.common.response.ErrorCode;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private ErrorCode errorCode;

    public BaseException() {
    }

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

    public BaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
