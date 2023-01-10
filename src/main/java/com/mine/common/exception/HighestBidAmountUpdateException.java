package com.mine.common.exception;

import com.mine.common.response.ErrorCode;

public class HighestBidAmountUpdateException extends BaseException {

    public HighestBidAmountUpdateException() {
        super(ErrorCode.COMMON_HIGHEST_BID_NOT_UPDATED);
    }

    public HighestBidAmountUpdateException(String message) {
        super(message, ErrorCode.COMMON_HIGHEST_BID_NOT_UPDATED);
    }
}