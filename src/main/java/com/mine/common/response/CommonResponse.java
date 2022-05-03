package com.mine.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class CommonResponse {

    private String message;
    private String errorCode;

    public CommonResponse(ErrorCode errorCode) {
        this.message = errorCode.getErrorMessage();
        this.errorCode = errorCode.name();
    }

    public CommonResponse(String message, String errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}