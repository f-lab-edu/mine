package com.mine.domain.bid;

import com.mine.domain.auction.Auction;
import com.mine.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Builder
@Getter
public class AutoBidCommand {

    private final long auctionId;
    private final long userId;
    private final long limit;

    public AutoBid toEntity() {
        return AutoBid.builder()
                .auction(Auction.builder().id(this.auctionId).build())
                .user(User.builder().id(this.userId).build())
                .limit(this.limit)
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
