package com.mine.infrastructure.bid;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RealtimeHighestBidResponse {

    private final long auctionId;

    private final long highestBidAmount;

    private final long incrementalBidAmount;
}
