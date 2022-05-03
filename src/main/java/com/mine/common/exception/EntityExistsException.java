package com.mine.common.exception;

import com.mine.common.response.ErrorCode;

public class EntityExistsException extends BaseException {

    public EntityExistsException() {
        super(ErrorCode.COMMON_ENTITY_EXISTS);
    }

    public EntityExistsException(String message) {
        super(message, ErrorCode.COMMON_ENTITY_EXISTS);
    }
}
