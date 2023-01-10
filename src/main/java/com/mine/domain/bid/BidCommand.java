package com.mine.domain.bid;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BidCommand {

    private final long auctionId;
    private final long userId;
    private final long amount;
}