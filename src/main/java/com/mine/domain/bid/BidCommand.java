package com.mine.domain.bid;

import com.mine.domain.auction.Auction;
import com.mine.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Builder
@Getter
public class BidCommand {

    private final long auctionId;
    private final long userId;
    private final long price;

    public BidHistory toEntity(ZonedDateTime biddingTime) {
        return BidHistory.builder()
                .auction(Auction.builder().id(this.auctionId).build())
                .user(User.builder().id(this.userId).build())
                .biddingPrice(this.price)
                .biddingTime(biddingTime)
                .build();
    }
}