package com.mine.common.exception;

import com.mine.common.response.ErrorCode;

public class HighestBidPriceUpdateException extends BaseException {

    public HighestBidPriceUpdateException() {
        super(ErrorCode.COMMON_HIGHEST_BID_NOT_UPDATED);
    }

    public HighestBidPriceUpdateException(String message) {
        super(message, ErrorCode.COMMON_HIGHEST_BID_NOT_UPDATED);
    }
}