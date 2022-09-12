package com.mine.common.exception;

import com.mine.common.response.ErrorCode;

public class AuctionAlreadyClosedException extends BaseException {
    public AuctionAlreadyClosedException() {
        super(ErrorCode.COMMON_AUCTION_ALREADY_CLOSED);
    }

    public AuctionAlreadyClosedException(String message) {
        super(message, ErrorCode.COMMON_AUCTION_ALREADY_CLOSED);
    }
}
