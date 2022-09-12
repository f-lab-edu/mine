package com.mine.common.response;

import com.mine.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CommonControllerAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<CommonResponse> handleBaseException(BaseException e) {
        log.error("handleBaseException", e);
        CommonResponse response = CommonResponse.builder()
                .message(e.getMessage())
                .errorCode(e.getErrorCode().name())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        BindingResult bindingResult = e.getBindingResult();
        FieldError fe = bindingResult.getFieldError();
        CommonResponse response;

        if (fe != null) {
            String message = "Request Error" + " " + fe.getField() + "=" + fe.getRejectedValue() + " (" + fe.getDefaultMessage() + ")";
            response = CommonResponse.builder()
                    .message(message)
                    .errorCode(ErrorCode.COMMON_INVALID_PARAMETER.name())
                    .build();
        } else {
            response = CommonResponse.builder()
                    .message(ErrorCode.COMMON_INVALID_PARAMETER.getErrorMessage())
                    .errorCode(ErrorCode.COMMON_INVALID_PARAMETER.name())
                    .build();
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
