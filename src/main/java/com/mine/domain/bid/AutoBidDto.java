package com.mine.domain.bid;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AutoBidDto {

    private final long auctionId;

    private final long userId;

    private final long limit;

    private final StorageType storageType;

    private final AutoBid autoBidMain;

    private final AutoBidCache autoBidCache;

    public AutoBidCache toAutoBidCacheEntity() {
        return AutoBidCache.builder()
                .auctionId(this.auctionId)
                .userId(this.userId)
                .limit(this.getLimit())
                .expiryTime(1)
                .build();
    }
}

