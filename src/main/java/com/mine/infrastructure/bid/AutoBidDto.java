package com.mine.infrastructure.bid;

import com.mine.domain.auction.Auction;
import com.mine.domain.bid.AutoBid;
import com.mine.domain.bid.AutoBidCache;
import com.mine.domain.bid.BidHistory;
import com.mine.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

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
                .build();
    }

    public BidHistory toBidHistoryEntity(ZonedDateTime biddingTime) {
        return BidHistory.builder()
                .auction(Auction.builder().id(this.auctionId).build())
                .user(User.builder().id(this.userId).build())
                .biddingPrice(this.limit)
                .biddingTime(biddingTime)
                .build();
    }

    public BidHistory toBidHistoryEntity(long nextMinimumBidAmount, ZonedDateTime biddingTime) {
        return BidHistory.builder()
                .auction(Auction.builder().id(this.auctionId).build())
                .user(User.builder().id(this.userId).build())
                .biddingPrice(nextMinimumBidAmount)
                .biddingTime(biddingTime)
                .build();
    }
}

